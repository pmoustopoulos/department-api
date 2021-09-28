package com.ainigma100.departmentapi.repository;

import com.ainigma100.departmentapi.entity.Department;
import com.ainigma100.departmentapi.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	List<Employee> findByDepartmentDepId(Integer departmentId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Employee emp " +
            "SET emp.department = null " +
            "WHERE emp.department = :department")
    void removeDepartmentFromEmployeeBy(Department department);
}
