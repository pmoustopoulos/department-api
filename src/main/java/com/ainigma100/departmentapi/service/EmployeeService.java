package com.ainigma100.departmentapi.service;

import com.ainigma100.departmentapi.dto.EmployeeAndDepartmentDTO;
import com.ainigma100.departmentapi.dto.EmployeeDTO;
import com.ainigma100.departmentapi.dto.EmployeeSearchCriteriaDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {

    EmployeeDTO createEmployee(Long departmentId, EmployeeDTO employeeDTO);

    Page<EmployeeDTO> getAllEmployeesUsingPagination(EmployeeSearchCriteriaDTO employeeSearchCriteriaDTO);

    List<EmployeeDTO> getEmployeesByDepartmentId(Long departmentId);

    EmployeeDTO getEmployeeById(Long departmentId, String employeeId);

    EmployeeDTO updateEmployeeById(Long departmentId, String employeeId, EmployeeDTO employeeDTO);

    void deleteEmployee(Long departmentId, String employeeId);

    EmployeeAndDepartmentDTO getEmployeeAndDepartmentByEmployeeEmail(String email);
}
