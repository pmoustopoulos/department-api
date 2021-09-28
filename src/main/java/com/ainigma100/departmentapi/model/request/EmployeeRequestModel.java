package com.ainigma100.departmentapi.model.request;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
public class EmployeeRequestModel implements Serializable {

    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private char sex;
    private Double salary;

}
