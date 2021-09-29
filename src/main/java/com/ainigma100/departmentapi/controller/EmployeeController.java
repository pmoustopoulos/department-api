package com.ainigma100.departmentapi.controller;

import com.ainigma100.departmentapi.model.request.EmployeeRequestModel;
import com.ainigma100.departmentapi.model.response.DepartmentResponseModel;
import com.ainigma100.departmentapi.model.response.EmployeeResponseModel;
import com.ainigma100.departmentapi.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	private final EmployeeService employeeService;


	/**
	 * This endpoint is used to get all the employees
	 * @return
	 */
	@GetMapping
	public ResponseEntity<List<EmployeeResponseModel>> getAllEmployees() {
		return employeeService.getAllEmployees();
	}


	/**
	 * This method is used to get all the employees using pagination
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/pagination")
	public Page<EmployeeResponseModel> getAllEmployeesUsingPagination(
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "size") Integer size
	) {
		return employeeService.getAllEmployeesUsingPagination(page, size);
	}


	/**
	 * This method is used to get an employee using the id of the employee
	 * @param employeeId
	 * @return
	 */
	@GetMapping("/{employeeId}")
	public ResponseEntity<EmployeeResponseModel> getEmployeeById(@PathVariable("employeeId") Integer employeeId) {
		return employeeService.getEmployeeById(employeeId);
	}


	/**
	 * This endpoint is used to create an employee
	 * @param employeeRequestModel
	 * @return
	 */
	@PostMapping
	public ResponseEntity<EmployeeResponseModel> createEmployee(
			@Valid @RequestBody EmployeeRequestModel employeeRequestModel) {

		return employeeService.createEmployee(employeeRequestModel);
	}


	/**
	 * This endpoint is used to delete an employee using the id of the employee
	 * @param employeeId
	 * @return
	 */
	@DeleteMapping("/{employeeId}")
	public ResponseEntity<String> deleteEmployeeById(@PathVariable("employeeId") Integer employeeId) {
		return employeeService.deleteEmployeeById(employeeId);
	}



}
