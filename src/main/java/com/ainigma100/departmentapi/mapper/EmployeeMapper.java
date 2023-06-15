package com.ainigma100.departmentapi.mapper;

import com.ainigma100.departmentapi.dto.EmployeeAndDepartmentDTO;
import com.ainigma100.departmentapi.dto.EmployeeDTO;
import com.ainigma100.departmentapi.dto.EmployeeReportDTO;
import com.ainigma100.departmentapi.dto.EmployeeRequestDTO;
import com.ainigma100.departmentapi.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {


    Employee employeeDtoToEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO employeeToEmployeeDto(Employee employee);

    List<Employee> employeeDtoToEmployee(List<EmployeeDTO> employeeDTOList);
    List<EmployeeDTO> employeeToEmployeeDto(List<Employee> employeeList);

    EmployeeDTO employeeRequestDTOToEmployeeDTO(EmployeeRequestDTO employeeRequestDTO);

    @Mapping(target = "departmentName", expression = "java(employee.getDepartment() != null ? employee.getDepartment().getDepartmentName() : null)")
    EmployeeReportDTO employeeToEmployeeReportDto(Employee employee);

    @Mapping(target = "departmentName", expression = "java(employeeList.getDepartment() != null ? employeeList.getDepartment().getDepartmentName() : null)")
    List<EmployeeReportDTO> employeeToEmployeeReportDto(List<Employee> employeeList);

    EmployeeAndDepartmentDTO employeeToEmployeeAndDepartmentDto(Employee employee);

    List<EmployeeAndDepartmentDTO> employeeToEmployeeAndDepartmentDto(List<Employee> employeeList);

}
