package com.ainigma100.departmentapi.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request, HttpServletRequest servletrequest) {
        log.error("Requested URL=" + servletrequest.getContextPath()	+ " from ip: " + servletrequest.getRemoteAddr());
        log.error("Exception Raised=" + ex);

        //ex.printStackTrace();
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        ErrorMessage error = new ErrorMessage("Server Error", details);

        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(NullPointerException ex, WebRequest request, HttpServletRequest servletrequest) {
        log.error(ex.getLocalizedMessage());
        log.error("Requested URL=" + servletrequest.getContextPath() + " from ip: " + servletrequest.getRemoteAddr());

        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        ErrorMessage error = new ErrorMessage("Null Pointer Exception", details);

        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request, HttpServletRequest servletrequest) {
        log.error(ex.getLocalizedMessage());
        log.error("Requested URL=" + servletrequest.getContextPath() + " from ip: " + servletrequest.getRemoteAddr());

        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        ErrorMessage error = new ErrorMessage("Contraint Violation", details);

        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
    }

    
    
    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException ex, WebRequest request, HttpServletRequest servletrequest) {
        log.error(ex.getLocalizedMessage());
        log.error("Requested URL=" + servletrequest.getContextPath() + " from ip: " + servletrequest.getRemoteAddr());

        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        ErrorMessage error = new ErrorMessage("Record Not Found", details);

        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
    
    
    @ExceptionHandler(RecordAlreadyExistException.class)
    public ResponseEntity<Object> handleRecordAlreadyExistException(RecordAlreadyExistException ex, WebRequest request, HttpServletRequest servletrequest) {
        log.error(ex.getLocalizedMessage());
        log.error("Requested URL=" + servletrequest.getContextPath() + " from ip: " + servletrequest.getRemoteAddr());

        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        ErrorMessage error = new ErrorMessage("Record Already Exist", details);

        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }



    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        
//        List<String> details = new ArrayList<>();
//
//        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
//            details.add(error.getDefaultMessage());
//        }
//
//        ErrorMessage error = new ErrorMessage("Validation Failed", details);
    	
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
        	
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            
        });
        
        log.error(errors.toString());

        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


}
