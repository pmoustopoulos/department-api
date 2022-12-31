package com.ainigma100.departmentapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequestDTO {


    @NotEmpty(message = "Department code should not be null or empty")
    @Size(min = 2, message = "Department code should have at least 2 characters")
    private String departmentCode;

    @NotEmpty(message = "Department name should not be null or empty")
    @Size(min = 2, message = "Department name should have at least 2 characters")
    private String departmentName;

    @NotEmpty(message = "Department description should not be null or empty")
    @Size(min = 10, message = "Department description should have at least 10 characters")
    private String departmentDescription;


}
