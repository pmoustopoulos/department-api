package com.ainigma100.departmentapi.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeAndDepartmentDTO {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal salary;
    private DepartmentDTO department;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DepartmentDTO {

        private Long id;
        private String departmentCode;
        private String departmentName;
        private String departmentDescription;

    }

}
