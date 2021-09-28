package com.ainigma100.departmentapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ainigma100.departmentapi.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	public List<Employee> findByDepartmentDepId(Integer departmentId);
	
}
