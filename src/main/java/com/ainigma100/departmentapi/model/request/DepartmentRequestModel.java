package com.ainigma100.departmentapi.model.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
public class DepartmentRequestModel implements Serializable {

    @NotEmpty(message = "depName cannot be null or empty")
    @NotBlank(message = "depName cannot have only whitespaces")
    @Size(max = 200, message = "depName can be maximum 200 characters")
    private String depName;

}
