package com.ainigma100.departmentapi.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentReportDTO {

    private Long id;
    private String departmentCode;
    private String departmentName;
    private String departmentDescription;
    private Integer totalEmployees;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
