package com.ainigma100.departmentapi.model.response;

import com.ainigma100.departmentapi.entity.Employee;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class DepartmentResponseModel implements Serializable {

    private Integer depId;
    private String depName;
    private Set<Employee> employees; // = new HashSet<>();

}
