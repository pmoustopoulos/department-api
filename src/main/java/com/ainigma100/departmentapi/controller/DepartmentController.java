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
	
	@GetMapping
	public ResponseEntity<List<DepartmentResponseModel>> getAllDepartments() {
		return departmentService.getAllDepartments();
	}


	@GetMapping("/pagination")
	public Page<DepartmentResponseModel> getAllDepartmentsUsingPagination(
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "size") Integer size
	) {
		return departmentService.getAllDepartmentsUsingPagination(page, size);
	}
	
	
	@GetMapping("/{departmentId}")
	public ResponseEntity<DepartmentResponseModel> getDepartmentById(@PathVariable("departmentId") Integer departmentId) {
		return departmentService.getDepartmentById(departmentId);
	}
	
	
	@PostMapping
	public ResponseEntity<DepartmentResponseModel> createDepartment(@Valid @RequestBody DepartmentRequestModel departmentRequestModel) {
		return departmentService.createDepartment(departmentRequestModel);
	}
	
	
	
	@DeleteMapping("/{departmentId}")
	public ResponseEntity<String> deleteDepartmentById(@PathVariable("departmentId") Integer departmentId) {
		return departmentService.deleteDepartmentById(departmentId);
	}

	@PutMapping("/{departmentId}/employee/{employeeId}")
	public ResponseEntity<DepartmentResponseModel> addEmployeeToDepartment(
			@PathVariable("departmentId") Integer departmentId,
			@PathVariable("employeeId") Integer employeeId) {

		return departmentService.addEmployeeToDepartment(departmentId, employeeId);
	}

}
