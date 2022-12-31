package com.ainigma100.departmentapi.service;

import com.ainigma100.departmentapi.dto.DepartmentDTO;
import com.ainigma100.departmentapi.dto.DepartmentSearchCriteriaDTO;
import org.springframework.data.domain.Page;

public interface DepartmentService {


    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);

    Page<DepartmentDTO> getAllDepartmentsUsingPagination(DepartmentSearchCriteriaDTO departmentSearchCriteriaDTO);

    DepartmentDTO getDepartmentById(Long id);

    DepartmentDTO updateDepartment(DepartmentDTO departmentDTO, Long id);

    void deleteDepartment(Long id);

}
