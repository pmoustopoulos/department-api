package com.ainigma100.departmentapi.repository;

import com.ainigma100.departmentapi.dto.DepartmentSearchCriteriaDTO;
import com.ainigma100.departmentapi.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Department findByDepartmentCode(String departmentCode);


    @Query(value = "select dep from Department dep " +
            "where ( :#{#criteria.departmentCode} IS NULL OR dep.departmentCode LIKE :#{#criteria.departmentCode}% ) " +
            "and ( :#{#criteria.departmentName} IS NULL OR dep.departmentName LIKE :#{#criteria.departmentName}% ) " +
            "and ( :#{#criteria.departmentDescription} IS NULL OR dep.departmentDescription LIKE %:#{#criteria.departmentDescription}% ) ")
    Page<Department> getAllDepartmentsUsingPagination(
            @Param("criteria") DepartmentSearchCriteriaDTO departmentSearchCriteriaDTO,
            Pageable pageable);

}
