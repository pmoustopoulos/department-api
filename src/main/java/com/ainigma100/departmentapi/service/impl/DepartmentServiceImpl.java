package com.ainigma100.departmentapi.service.impl;

import com.ainigma100.departmentapi.dto.DepartmentDTO;
import com.ainigma100.departmentapi.dto.DepartmentSearchCriteriaDTO;
import com.ainigma100.departmentapi.entity.Department;
import com.ainigma100.departmentapi.exception.ResourceAlreadyExistException;
import com.ainigma100.departmentapi.exception.ResourceNotFoundException;
import com.ainigma100.departmentapi.mapper.DepartmentMapper;
import com.ainigma100.departmentapi.repository.DepartmentRepository;
import com.ainigma100.departmentapi.service.DepartmentService;
import com.ainigma100.departmentapi.utils.SortItem;
import com.ainigma100.departmentapi.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    private static final String DEPARTMENT = "Department";


    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {

        Department recordFromDB = departmentRepository.findByDepartmentCode(departmentDTO.getDepartmentCode());

        if (recordFromDB != null) {
            throw new ResourceAlreadyExistException(DEPARTMENT, "departmentCode", departmentDTO.getDepartmentCode());
        }

        Department recordToBeSaved = departmentMapper.departmentDtoToDepartment(departmentDTO);

        Department savedRecord = departmentRepository.save(recordToBeSaved);

        return departmentMapper.departmentToDepartmentDto(savedRecord);
    }

    @Override
    public Page<DepartmentDTO> getAllDepartmentsUsingPagination(
            DepartmentSearchCriteriaDTO departmentSearchCriteriaDTO) {


        Integer page = departmentSearchCriteriaDTO.getPage();
        Integer size = departmentSearchCriteriaDTO.getSize();
        List<SortItem> sortList = departmentSearchCriteriaDTO.getSortList();

        // this pageable will be used for the pagination.
        Pageable pageable = Utils.createPageableBasedOnPageAndSizeAndSorting(sortList, page, size);

        Page<Department> recordsFromDb = departmentRepository.getAllDepartmentsUsingPagination(departmentSearchCriteriaDTO, pageable);

        List<DepartmentDTO> result = departmentMapper.departmentToDepartmentDto(recordsFromDb.getContent());

        return new PageImpl<>(result, pageable, recordsFromDb.getTotalElements());
    }

    @Override
    public DepartmentDTO getDepartmentById(Long id) {


        Department recordFromDB = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(DEPARTMENT, "id", id));

        return departmentMapper.departmentToDepartmentDto(recordFromDB);
    }

    @Override
    public DepartmentDTO updateDepartment(DepartmentDTO departmentDTO, Long id) {


        Department recordFromDB = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(DEPARTMENT, "id", id));

        // just to be safe that the object does not have another id
        departmentDTO.setId(id);


        Department recordToBeSaved = departmentMapper.departmentDtoToDepartment(departmentDTO);
        // I had to set again the employees otherwise I would lose the reference
        recordToBeSaved.setEmployees(recordFromDB.getEmployees());

        Department savedRecord = departmentRepository.save(recordToBeSaved);

        return departmentMapper.departmentToDepartmentDto(savedRecord);
    }

    @Override
    public void deleteDepartment(Long id) {

        Department recordFromDB = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(DEPARTMENT, "id", id));

        departmentRepository.delete(recordFromDB);

    }
}
