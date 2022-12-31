package com.ainigma100.departmentapi.mapper;

import com.ainigma100.departmentapi.dto.DepartmentDTO;
import com.ainigma100.departmentapi.dto.DepartmentReportDTO;
import com.ainigma100.departmentapi.dto.DepartmentRequestDTO;
import com.ainigma100.departmentapi.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EmployeeMapper.class})
public interface DepartmentMapper {

    Department departmentDtoToDepartment(DepartmentDTO departmentDTO);
    DepartmentDTO departmentToDepartmentDto(Department department);

    List<Department> departmentDtoToDepartment(List<DepartmentDTO> departmentDTOList);
    List<DepartmentDTO> departmentToDepartmentDto(List<Department> departmentList);

    DepartmentDTO departmentRequestDTOToDepartmentDTO(DepartmentRequestDTO departmentRequestDTO);

    @Mapping(target = "totalEmployees", expression = "java(department.getEmployees() != null ? department.getEmployees().size() : 0)")
    DepartmentReportDTO departmentToDepartmentReportDto(Department department);

    @Mapping(target = "totalEmployees", expression = "java(departmentList.getEmployees() != null ? departmentList.getEmployees().size() : 0)")
    List<DepartmentReportDTO> departmentToDepartmentReportDto(List<Department> departmentList);

}
