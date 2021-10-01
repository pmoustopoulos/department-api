package com.ainigma100.departmentapi.model.request;

import com.ainigma100.departmentapi.util.annotation.Sex;
import lombok.Getter;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
public class EmployeeRequestModel implements Serializable {

    @NotEmpty(message = "firstName cannot be null or empty")
    @NotBlank(message = "firstName cannot have only whitespaces")
    @Size(min = 2, max = 50, message = "firstName can be between 2 and 50 characters")
    private String firstName;

    @NotEmpty(message = "lastName cannot be null or empty")
    @NotBlank(message = "lastName cannot have only whitespaces")
    @Size(min = 2, max = 200, message = "lastName can be between 2 and 200 characters")
    private String lastName;

    @NotEmpty(message = "email cannot be null or empty")
    @NotBlank(message = "email cannot have only whitespaces")
//    @Email    I did not use this annotation because it does not validate correctly
    @Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", message = "Email is not valid")
    private String email;

    @Past(message = "birthDate should have a past value")
    private LocalDate birthDate;

    @NotNull(message = "sex cannot be null")
    @Sex
    private String sex;

    @NotNull(message = "salary cannot be null")
    private Double salary;

}
