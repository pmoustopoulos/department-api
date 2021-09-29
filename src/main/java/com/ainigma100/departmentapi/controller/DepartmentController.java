package com.ainigma100.departmentapi.controller;

import com.ainigma100.departmentapi.model.request.DepartmentRequestModel;
import com.ainigma100.departmentapi.model.response.DepartmentResponseModel;
import com.ainigma100.departmentapi.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

	private final DepartmentService departmentService;

	/**
	 * This endpoint is used to get all the departments
	 * @return
	 */
	@GetMapping
	public ResponseEntity<List<DepartmentResponseModel>> getAllDepartments() {
		return departmentService.getAllDepartments();
	}


	/**
	 * This method is used to get all the departments using pagination
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/pagination")
	public Page<DepartmentResponseModel> getAllDepartmentsUsingPagination(
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "size") Integer size
	) {
		return departmentService.getAllDepartmentsUsingPagination(page, size);
	}


	/**
	 * This method is used to get a department using the id of the department
	 * @param departmentId
	 * @return
	 */
	@GetMapping("/{departmentId}")
	public ResponseEntity<DepartmentResponseModel> getDepartmentById(@PathVariable("departmentId") Integer departmentId) {
		return departmentService.getDepartmentById(departmentId);
	}


	/**
	 * This endpoint is used to create a department
	 * @param departmentRequestModel
	 * @return
	 */
	@PostMapping
	public ResponseEntity<DepartmentResponseModel> createDepartment(@Valid @RequestBody DepartmentRequestModel departmentRequestModel) {
		return departmentService.createDepartment(departmentRequestModel);
	}


	/**
	 * This method is used to delete a department using the id of the department
	 * @param departmentId
	 * @return
	 */
	@DeleteMapping("/{departmentId}")
	public ResponseEntity<String> deleteDepartmentById(@PathVariable("departmentId") Integer departmentId) {
		return departmentService.deleteDepartmentById(departmentId);
	}

	/**
	 * This endppoint is used to add an employee to a department
	 * @param departmentId
	 * @param employeeId
	 * @return
	 */
	@PutMapping("/{departmentId}/employee/{employeeId}")
	public ResponseEntity<DepartmentResponseModel> addEmployeeToDepartment(
			@PathVariable("departmentId") Integer departmentId,
			@PathVariable("employeeId") Integer employeeId) {

		return departmentService.addEmployeeToDepartment(departmentId, employeeId);
	}

}
