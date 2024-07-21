package com.ainigma100.departmentapi.exception;

import com.ainigma100.departmentapi.dto.APIResponse;
import com.ainigma100.departmentapi.dto.ErrorDTO;
import com.ainigma100.departmentapi.enums.Status;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.angus.mail.util.MailConnectException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
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

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({RuntimeException.class, NullPointerException.class})
    public ResponseEntity<Object> handleRuntimeExceptions(RuntimeException exception) {

        log.error(exception.getMessage());

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "An internal server error occurred")));

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundExceptions(ResourceNotFoundException exception) {

        log.error(exception.getMessage());

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "The requested resource was not found")));

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ResourceAlreadyExistException.class, BusinessLogicException.class, DataAccessException.class})
    public ResponseEntity<Object> handleOtherExceptions(Exception exception) {

        log.error(exception.getMessage());

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "An error occurred while processing your request")));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {

        log.error(exception.getMessage());

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "The requested URL does not support this method")));

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
                String errorMessage = error.getDefaultMessage();
                errors.add(new ErrorDTO(fieldName, errorMessage));
            });

        } else if (exception instanceof MissingServletRequestParameterException ex) {

            String parameterName = ex.getParameterName();
            errors.add(new ErrorDTO("", "Required parameter is missing: " + parameterName));

        } else if (exception instanceof MissingPathVariableException ex) {

            String variableName = ex.getVariableName();
            errors.add(new ErrorDTO("", "Missing path variable: " + variableName));
        }

        response.setErrors(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<Object> handleMessagingException(MessagingException exception) {

        log.error(exception.getMessage());

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "An internal server error occurred")));

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MailSendException.class)
    public ResponseEntity<Object> handleMailSendException(MailSendException exception) {

        log.error("Mail server connection failed", exception);

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "Mail server connection failed. Email-related functionality will not work.")));

        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(MailConnectException.class)
    public ResponseEntity<Object> handleMailConnectException(MailConnectException exception) {

        log.error("Mail server connection failed", exception);

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "Mail server connection failed. Email-related functionality will not work.")));

        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

}

