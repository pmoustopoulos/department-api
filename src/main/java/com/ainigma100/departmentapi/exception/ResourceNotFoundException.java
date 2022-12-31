package com.ainigma100.departmentapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private String resourceName;
    private String fieldName;
    private Long fieldValue;
    private String fieldValueAsString;

    public ResourceNotFoundException(String resourceName, String fieldName, Long fieldValue) {

        super(String.format("%s with %s : '%s' not found", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValueAsString) {

        super(String.format("%s with %s : '%s' not found", resourceName, fieldName, fieldValueAsString));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValueAsString = fieldValueAsString;
    }

}
