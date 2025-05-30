package com.ainigma100.departmentapi.service.impl;

import com.ainigma100.departmentapi.dto.EmployeeAndDepartmentDTO;
import com.ainigma100.departmentapi.dto.EmployeeDTO;
import com.ainigma100.departmentapi.dto.EmployeeSearchCriteriaDTO;
import com.ainigma100.departmentapi.entity.Department;
import com.ainigma100.departmentapi.entity.Employee;
import com.ainigma100.departmentapi.exception.BusinessLogicException;
import com.ainigma100.departmentapi.mapper.EmployeeMapper;
import com.ainigma100.departmentapi.repository.DepartmentRepository;
import com.ainigma100.departmentapi.repository.EmployeeRepository;
import com.ainigma100.departmentapi.service.EmployeeService;
import com.ainigma100.departmentapi.utils.SortItem;
import com.ainigma100.departmentapi.utils.Utils;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    private static final String EMPLOYEE_NOT_BELONG_TO_DEPARTMENT = "Employee does not belong to Department";


    @Override
    public EmployeeDTO createEmployee(Long departmentId, EmployeeDTO employeeDTO) {

        Employee employeeRecordFromDB = employeeRepository.findByEmail(employeeDTO.getEmail());

        if (employeeRecordFromDB != null) {
            throw new EntityExistsException("Employee with email '" + employeeDTO.getEmail() + "' already exists");
        }

        Employee employee = employeeMapper.employeeDtoToEmployee(employeeDTO);

        Department departmentRecordFromDB = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department with id '" + departmentId + "' not found"));

        employee.setDepartment(departmentRecordFromDB);

        Employee savedEmployee = employeeRepository.save(employee);

        return employeeMapper.employeeToEmployeeDto(savedEmployee);
    }

    @Override
    public Page<EmployeeDTO> getAllEmployeesUsingPagination(EmployeeSearchCriteriaDTO employeeSearchCriteriaDTO) {

        Integer page = employeeSearchCriteriaDTO.getPage();
        Integer size = employeeSearchCriteriaDTO.getSize();
        List<SortItem> sortList = employeeSearchCriteriaDTO.getSortList();

        // this pageable will be used for the pagination.
        Pageable pageable = Utils.createPageableBasedOnPageAndSizeAndSorting(sortList, page, size);

        Page<Employee> recordsFromDb = employeeRepository.getAllEmployeesUsingPagination(employeeSearchCriteriaDTO, pageable);

        List<EmployeeDTO> result = employeeMapper.employeeToEmployeeDto(recordsFromDb.getContent());

        return new PageImpl<>(result, pageable, recordsFromDb.getTotalElements());
    }


    @Override
    public List<EmployeeDTO> getEmployeesByDepartmentId(Long departmentId) {

        List<Employee> employeesFromDB = employeeRepository.findByDepartmentId(departmentId);

        return employeeMapper.employeeToEmployeeDto(employeesFromDB);
    }


    @Override
    public EmployeeDTO getEmployeeById(Long departmentId, String employeeId) {

        Department departmentRecordFromDB = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department with id '" + departmentId + "' not found"));

        Employee employeeRecordFromDB = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee with id '" + employeeId + "' not found"));


        if (!employeeBelongsToDepartment(departmentRecordFromDB, employeeRecordFromDB)) {
            throw new BusinessLogicException(EMPLOYEE_NOT_BELONG_TO_DEPARTMENT);
        }

        return employeeMapper.employeeToEmployeeDto(employeeRecordFromDB);
    }

    @Override
    public EmployeeDTO updateEmployeeById(Long departmentId, String employeeId, EmployeeDTO employeeDTO) {

        Department departmentRecordFromDB = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department with id '" + departmentId + "' not found"));

        Employee employeeRecordFromDB = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee with id '" + employeeId + "' not found"));

        if (!employeeBelongsToDepartment(departmentRecordFromDB, employeeRecordFromDB)) {
            throw new BusinessLogicException(EMPLOYEE_NOT_BELONG_TO_DEPARTMENT);
        }


        // just to be safe that the object does not have another id
        employeeDTO.setId(employeeId);

        Employee recordToBeSaved = employeeMapper.employeeDtoToEmployee(employeeDTO);

        // assign the post to the current comment
        recordToBeSaved.setDepartment(departmentRecordFromDB);

        Employee savedEmployeeRecord = employeeRepository.save(recordToBeSaved);

        return employeeMapper.employeeToEmployeeDto(savedEmployeeRecord);
    }

    @Override
    public void deleteEmployee(Long departmentId, String employeeId) {

        Department departmentRecordFromDB = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department with id '" + departmentId + "' not found"));

        Employee employeeRecordFromDB = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee with id '" + employeeId + "' not found"));

        if (!employeeBelongsToDepartment(departmentRecordFromDB, employeeRecordFromDB)) {
            throw new BusinessLogicException(EMPLOYEE_NOT_BELONG_TO_DEPARTMENT);
        }

        employeeRepository.delete(employeeRecordFromDB);

    }

    @Override
    public EmployeeAndDepartmentDTO getEmployeeAndDepartmentByEmployeeEmail(String email) {

        Employee employeeRecordFromDB = employeeRepository.getEmployeeAndDepartmentByEmployeeEmail(email);

        if (employeeRecordFromDB == null) {
            throw new EntityNotFoundException("Employee with email '" + email + "' not found");
        }

        return employeeMapper.employeeToEmployeeAndDepartmentDto(employeeRecordFromDB);
    }


    private boolean employeeBelongsToDepartment(Department departmentRecordFromDB, Employee employeeRecordFromDB) {

        if (departmentRecordFromDB == null || employeeRecordFromDB.getDepartment() == null) {
            return false;
        }

        Long departmentId = employeeRecordFromDB.getDepartment().getId();
        return departmentId != null && departmentId.equals(departmentRecordFromDB.getId());
    }


}
