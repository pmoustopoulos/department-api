package com.ainigma100.departmentapi.exception;

import com.ainigma100.departmentapi.dto.APIResponse;
import com.ainigma100.departmentapi.dto.ErrorDTO;
import com.ainigma100.departmentapi.enums.Status;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Environment environment;

    /**
     * Checks if the application is running in production mode.
     * Returns true if the active profile is 'prod' or 'production'.
     */
    private boolean isProduction() {
        return Stream.of(environment.getActiveProfiles())
                .anyMatch(profile -> profile.equalsIgnoreCase("prod") || profile.equalsIgnoreCase("production"));
    }


    @ExceptionHandler({RuntimeException.class, NullPointerException.class})
    public ResponseEntity<Object> handleRuntimeExceptions(RuntimeException exception) {

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());

        String errorMessage = isProduction() ? "An internal server error occurred" : exception.getMessage();
        response.setErrors(Collections.singletonList(new ErrorDTO("", errorMessage)));

        log.error("RuntimeException or NullPointerException occurred {}", exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());

        String errorMessage = isProduction() ? "Method not supported" : "The requested URL does not support this method";
        response.setErrors(Collections.singletonList(new ErrorDTO("", errorMessage)));

        log.error("HttpRequestMethodNotSupportedException occurred {}", exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }


    @ExceptionHandler({MethodArgumentNotValidException.class, MissingServletRequestParameterException.class, MissingPathVariableException.class})
    public ResponseEntity<Object> handleValidationExceptions(Exception exception) {

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());

        List<ErrorDTO> errors = new ArrayList<>();
        if (exception instanceof MethodArgumentNotValidException ex) {

            ex.getBindingResult().getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = isProduction() ? "Invalid input value" : error.getDefaultMessage();
                errors.add(new ErrorDTO(fieldName, errorMessage));
            });

        } else if (exception instanceof MissingServletRequestParameterException ex) {

            String errorMessage = isProduction() ? "Required parameter is missing" : "Missing parameter: " + ex.getParameterName();
            errors.add(new ErrorDTO("", errorMessage));

        } else if (exception instanceof MissingPathVariableException ex) {
            String errorMessage = isProduction() ? "Missing path variable" : "Missing path variable: " + ex.getVariableName();
            errors.add(new ErrorDTO("", errorMessage));
        }

        log.error("Validation errors: {}", errors);

        response.setErrors(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<APIResponse<ErrorDTO>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());

        String errorMessage = isProduction() ? "Invalid request format" : "Malformed JSON request";
        response.setErrors(Collections.singletonList(new ErrorDTO("", errorMessage)));

        log.error("Malformed JSON request: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIResponse<ErrorDTO>> handleConstraintViolationException(ConstraintViolationException ex) {

        List<ErrorDTO> errors = new ArrayList<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(new ErrorDTO(violation.getPropertyPath().toString(),
                    isProduction() ? "Invalid input data" : violation.getMessage()));
        }

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(errors);

        log.error("Constraint violation errors: {}", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundExceptions(EntityNotFoundException exception) {

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());

        String errorMessage = isProduction() ? "The requested resource was not found" : exception.getMessage();
        response.setErrors(Collections.singletonList(new ErrorDTO("", errorMessage)));

        log.error("EntityNotFoundException occurred {}", exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<APIResponse<ErrorDTO>> handleEntityExistsException(EntityExistsException exception) {

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());

        String errorMessage = isProduction() ? "The entity already exists" : exception.getMessage();
        response.setErrors(Collections.singletonList(new ErrorDTO("", errorMessage)));

        log.error("EntityExistsException occurred: {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
