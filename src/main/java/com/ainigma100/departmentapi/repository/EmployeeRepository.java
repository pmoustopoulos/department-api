package com.ainigma100.departmentapi.repository;

import com.ainigma100.departmentapi.dto.EmployeeSearchCriteriaDTO;
import com.ainigma100.departmentapi.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    List<Employee> findByDepartmentId(Long departmentId);

    @Query(value = "select emp from Employee emp " +
            "where ( :#{#criteria.firstName} IS NULL OR emp.firstName LIKE :#{#criteria.firstName}% ) " +
            "and ( :#{#criteria.lastName} IS NULL OR emp.lastName LIKE :#{#criteria.lastName}% ) " +
            "and ( :#{#criteria.email} IS NULL OR emp.email LIKE %:#{#criteria.email}% ) ")
    Page<Employee> getAllEmployeesUsingPagination(
            @Param("criteria") EmployeeSearchCriteriaDTO employeeSearchCriteriaDTO,
            Pageable pageable);


    Employee findByEmail(String email);
}
