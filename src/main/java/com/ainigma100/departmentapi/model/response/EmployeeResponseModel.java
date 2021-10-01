package com.ainigma100.departmentapi.model.response;

import com.ainigma100.departmentapi.entity.Department;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class EmployeeResponseModel implements Serializable {

    private Integer empId;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private char sex;
    private Double salary;
    private Department department;

}
