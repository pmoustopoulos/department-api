package com.ainigma100.departmentapi.controller;

import com.ainigma100.departmentapi.dto.*;
import com.ainigma100.departmentapi.enums.Status;
import com.ainigma100.departmentapi.mapper.EmployeeMapper;
import com.ainigma100.departmentapi.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;




    @Operation(summary = "Add a new employee to the specific department")
    @PostMapping("/departments/{departmentId}/employees")
    @PreAuthorize("hasAnyRole('client_admin', 'client_user')")
    public ResponseEntity<APIResponse<EmployeeDTO>> createEmployee(
            @PathVariable("departmentId") Long departmentId,
            @Valid @RequestBody EmployeeRequestDTO employeeRequestDTO) {

        EmployeeDTO employeeDTO = employeeMapper.employeeRequestDTOToEmployeeDTO(employeeRequestDTO);

        EmployeeDTO result = employeeService.createEmployee(departmentId, employeeDTO);

        // Builder Design pattern
        APIResponse<EmployeeDTO> responseDTO = APIResponse
                .<EmployeeDTO>builder()
                .status(Status.SUCCESS.getValue())
                .results(result)
                .build();


        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }


    @Operation(summary = "Search all employees from all departments using pagination",
            description = "Returns a list of employees belonging to any department")
    @PostMapping("/employees/search")
    @PreAuthorize("hasAnyRole('client_admin', 'client_user')")
    public ResponseEntity<APIResponse<Page<EmployeeDTO>>> getAllEmployeesUsingPagination(
            @Valid @RequestBody EmployeeSearchCriteriaDTO employeeSearchCriteriaDTO) {

        Page<EmployeeDTO> result = employeeService.getAllEmployeesUsingPagination(employeeSearchCriteriaDTO);

        // Builder Design pattern
        APIResponse<Page<EmployeeDTO>> responseDTO = APIResponse
                .<Page<EmployeeDTO>>builder()
                .status(Status.SUCCESS.getValue())
                .results(result)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }


    @Operation(summary = "Find all employees belonging to the specific department",
            description = "Returns a list of employees belonging to the specific department")
    @GetMapping("/departments/{departmentId}/employees")
    @PreAuthorize("hasAnyRole('client_admin', 'client_user')")
    public ResponseEntity<APIResponse<List<EmployeeDTO>>> getEmployeesByDepartmentId(
            @PathVariable("departmentId") Long departmentId) {

        List<EmployeeDTO> result = employeeService.getEmployeesByDepartmentId(departmentId);

        // Builder Design pattern
        APIResponse<List<EmployeeDTO>> responseDTO = APIResponse
                .<List<EmployeeDTO>>builder()
                .status(Status.SUCCESS.getValue())
                .results(result)
                .build();


        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }


    @Operation(summary = "Find employee by ID belonging to the specific department",
            description = "Returns a single employee belonging to the specific department")
    @GetMapping("/departments/{departmentId}/employees/{employeeId}")
    @PreAuthorize("hasAnyRole('client_admin', 'client_user')")
    public ResponseEntity<APIResponse<EmployeeDTO>> getEmployeeById(
            @PathVariable("departmentId") Long departmentId,
            @PathVariable("employeeId") String employeeId) {

        EmployeeDTO result = employeeService.getEmployeeById(departmentId, employeeId);

        // Builder Design pattern
        APIResponse<EmployeeDTO> responseDTO = APIResponse
                .<EmployeeDTO>builder()
                .status(Status.SUCCESS.getValue())
                .results(result)
                .build();


        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }


    @Operation(summary = "Update an existing employee belonging to the specific department")
    @PutMapping("/departments/{departmentId}/employees/{employeeId}")
    @PreAuthorize("hasAnyRole('client_admin', 'client_user')")
    public ResponseEntity<APIResponse<EmployeeDTO>> updateEmployeeById(
            @PathVariable("departmentId") Long departmentId,
            @PathVariable("employeeId") String employeeId,
            @Valid @RequestBody EmployeeRequestDTO employeeRequestDTO) {

        EmployeeDTO employeeDTO = employeeMapper.employeeRequestDTOToEmployeeDTO(employeeRequestDTO);

        EmployeeDTO result = employeeService.updateEmployeeById(departmentId, employeeId, employeeDTO);

        // Builder Design pattern
        APIResponse<EmployeeDTO> responseDTO = APIResponse
                .<EmployeeDTO>builder()
                .status(Status.SUCCESS.getValue())
                .results(result)
                .build();


        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }


    @Operation(summary = "Delete an employee belonging to the specific department.",
            description = "Note: You must be an admin to delete a record")
    @DeleteMapping("/departments/{departmentId}/employees/{employeeId}")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<APIResponse<String>> deleteEmployee(
            @PathVariable("departmentId") Long departmentId,
            @PathVariable("employeeId") String employeeId) {

        employeeService.deleteEmployee(departmentId, employeeId);

        String result = "Employee deleted successfully";

        // Builder Design pattern
        APIResponse<String> responseDTO = APIResponse
                .<String>builder()
                .status(Status.SUCCESS.getValue())
                .results(result)
                .build();


        return new ResponseEntity<>(responseDTO, HttpStatus.OK);

    }

    @Operation(summary = "Get an employee along with the department using the employee email. " +
            "Note: This endpoint was created just to demonstrate how to get a child and a parent object using the child's property")
    @GetMapping("/employees")
    @PreAuthorize("hasAnyRole('client_admin', 'client_user')")
    public ResponseEntity<APIResponse<EmployeeAndDepartmentDTO>> getEmployeeAndDepartmentByEmployeeEmail(
            @RequestParam("email") String email) {

        EmployeeAndDepartmentDTO result = employeeService.getEmployeeAndDepartmentByEmployeeEmail(email);

        // Builder Design pattern
        APIResponse<EmployeeAndDepartmentDTO> responseDTO = APIResponse
                .<EmployeeAndDepartmentDTO>builder()
                .status(Status.SUCCESS.getValue())
                .results(result)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }


}
