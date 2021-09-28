package com.ainigma100.departmentapi.controller;

import com.ainigma100.departmentapi.model.request.EmployeeRequestModel;
import com.ainigma100.departmentapi.model.response.EmployeeResponseModel;
import com.ainigma100.departmentapi.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	private final EmployeeService employeeService;


	@GetMapping
	public ResponseEntity<List<EmployeeResponseModel>> getAllEmployees() {
		return employeeService.getAllEmployees();
	}

	@GetMapping("/{employeeId}")
	public ResponseEntity<EmployeeResponseModel> getEmployeeById(@PathVariable("employeeId") Integer employeeId) {
		return employeeService.getEmployeeById(employeeId);
	}


	@PostMapping
	public ResponseEntity<EmployeeResponseModel> createEmployee(
			@Valid @RequestBody EmployeeRequestModel employeeRequestModel) {

		return employeeService.createEmployee(employeeRequestModel);
	}


	@DeleteMapping("/{employeeId}")
	public ResponseEntity<String> deleteEmployeeById(@PathVariable("employeeId") Integer employeeId) {
		return employeeService.deleteEmployeeById(employeeId);
	}



}
