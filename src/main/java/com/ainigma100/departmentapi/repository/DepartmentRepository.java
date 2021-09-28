package com.ainigma100.departmentapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ainigma100.departmentapi.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer>{

	Department findByDepNameIgnoreCase(String depName);

}
