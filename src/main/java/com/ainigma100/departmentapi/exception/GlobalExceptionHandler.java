package com.ainigma100.departmentapi.exception;

import com.ainigma100.departmentapi.dto.APIResponse;
import com.ainigma100.departmentapi.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private final String FAILED = "Failed";



    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception exception) {

        log.error(exception.getMessage());

        APIResponse<ErrorDTO> response = new APIResponse<>();

        response.setStatus(FAILED);
        response.setErrors(Collections.singletonList(new ErrorDTO("", exception.getMessage())));

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(NullPointerException exception) {

        log.error(exception.getMessage());

        APIResponse<ErrorDTO> response = new APIResponse<>();

        response.setStatus(FAILED);
        response.setErrors(Collections.singletonList(new ErrorDTO("", exception.getMessage())));

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException exception) {

        log.error(exception.getMessage());

        APIResponse<ErrorDTO> response = new APIResponse<>();

        response.setStatus(FAILED);
        response.setErrors(Collections.singletonList(new ErrorDTO("", exception.getMessage())));

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<?> handleResourceAlreadyExistException(ResourceAlreadyExistException exception) {

        log.error(exception.getMessage());

        APIResponse<ErrorDTO> response = new APIResponse<>();

        response.setStatus(FAILED);
        response.setErrors(Collections.singletonList(new ErrorDTO("", exception.getMessage())));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<?> handleBlogAPIException(BusinessLogicException exception) {

        log.error(exception.getMessage());

        APIResponse<ErrorDTO> response = new APIResponse<>();

        response.setStatus(FAILED);
        response.setErrors(Collections.singletonList(new ErrorDTO("", exception.getMessage())));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessException(DataAccessException exception) {

        log.error(exception.getMessage());

        APIResponse<?> response = new APIResponse<>();

        response.setStatus(FAILED);
        response.setErrors(Collections.singletonList(new ErrorDTO("", exception.getMessage())));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



    /**
     * This method is used to handle the errors that occur if the annotations above the fields are not satisfied
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {

        List<ErrorDTO> errors = new ArrayList<>();

        exception.getBindingResult().getAllErrors().forEach(error -> {

            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add( new ErrorDTO( fieldName, errorMessage) );

        });

        APIResponse<?> response = new APIResponse<>();

        response.setStatus(FAILED);
        response.setErrors(errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException exception) {

        log.error(exception.getMessage());

        String parameterName = exception.getParameterName();

        APIResponse<?> response = new APIResponse<>();

        response.setStatus(FAILED);
        response.setErrors(Collections.singletonList(new ErrorDTO(parameterName, exception.getMessage())));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<Object> handleMissingPathVariableException(
            MissingPathVariableException exception) {

        log.error(exception.getMessage());

        String variableName = exception.getVariableName();

        APIResponse<?> response = new APIResponse<>();

        response.setStatus(FAILED);
        response.setErrors(Collections.singletonList(new ErrorDTO(variableName, exception.getMessage())));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }


}
