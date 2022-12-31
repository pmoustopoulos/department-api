package com.ainigma100.departmentapi.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeReportDTO {

    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal salary;
    private String departmentName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
