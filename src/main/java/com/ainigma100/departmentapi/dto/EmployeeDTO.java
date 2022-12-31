package com.ainigma100.departmentapi.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal salary;

}
