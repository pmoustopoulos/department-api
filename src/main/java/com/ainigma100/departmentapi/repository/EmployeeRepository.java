package com.ainigma100.departmentapi.repository;

import com.ainigma100.departmentapi.dto.EmployeeSearchCriteriaDTO;
import com.ainigma100.departmentapi.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    List<Employee> findByDepartmentId(Long departmentId);

    @Query(value = """
            select emp from Employee emp
            where ( :#{#criteria.firstName} IS NULL OR LOWER(emp.firstName) LIKE LOWER( CONCAT(:#{#criteria.firstName}, '%') ) )
            and ( :#{#criteria.lastName} IS NULL OR LOWER(emp.lastName) LIKE LOWER( CONCAT(:#{#criteria.lastName}, '%') ) )
            and ( :#{#criteria.email} IS NULL OR LOWER(emp.email) LIKE LOWER( CONCAT('%', :#{#criteria.email}, '%') ) )
            """)
    Page<Employee> getAllEmployeesUsingPagination(
            @Param("criteria") EmployeeSearchCriteriaDTO employeeSearchCriteriaDTO,
            Pageable pageable);

    @EntityGraph(attributePaths = "department")
    @Query(value = """
            select emp from Employee emp
            where emp.email = :email
            """)
    Employee getEmployeeAndDepartmentByEmployeeEmail(@Param("email") String email);

    Employee findByEmail(String email);
}
