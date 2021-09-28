package com.ainigma100.departmentapi.repository;

import com.ainigma100.departmentapi.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	List<Employee> findByDepartmentDepId(Integer departmentId);
	
}
