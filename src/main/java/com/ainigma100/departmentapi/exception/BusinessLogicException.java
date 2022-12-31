package com.ainigma100.departmentapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BusinessLogicException extends RuntimeException {


    private String message;

    public BusinessLogicException(String message) {
        this.message = message;
    }


}
