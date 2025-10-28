package com.ainigma100.departmentapi.service.impl;

import com.ainigma100.departmentapi.dto.DepartmentDTO;
import com.ainigma100.departmentapi.dto.DepartmentSearchCriteriaDTO;
import com.ainigma100.departmentapi.entity.Department;
import com.ainigma100.departmentapi.mapper.DepartmentMapper;
import com.ainigma100.departmentapi.repository.DepartmentRepository;
import com.ainigma100.departmentapi.service.DepartmentService;
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
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;


    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {

        Department recordFromDB = departmentRepository.findByDepartmentCode(departmentDTO.getDepartmentCode());

        if (recordFromDB != null) {
            throw new EntityExistsException("Department with departmentCode '" + departmentDTO.getDepartmentCode() + "' already exists");
        }

        Department recordToBeSaved = departmentMapper.departmentDtoToDepartment(departmentDTO);

        Department savedRecord = departmentRepository.save(recordToBeSaved);

        return departmentMapper.departmentToDepartmentDto(savedRecord);
    }

    @Override
    public Page<DepartmentDTO> getAllDepartmentsUsingPagination(DepartmentSearchCriteriaDTO departmentSearchCriteriaDTO) {

        // defensive defaults
        Integer page = departmentSearchCriteriaDTO.getPage() == null ? 0 : departmentSearchCriteriaDTO.getPage();
        Integer size = departmentSearchCriteriaDTO.getSize() == null ? 10 : departmentSearchCriteriaDTO.getSize();
        List<SortItem> sortList = departmentSearchCriteriaDTO.getSortList();

        // create Pageable using existing utility (keeps your sorting logic)
        Pageable pageable = Utils.createPageableBasedOnPageAndSizeAndSorting(sortList, page, size);

        // Build Specification from criteria
        var spec = com.ainigma100.departmentapi.specification.DepartmentSpecification.fromCriteria(departmentSearchCriteriaDTO);

        // Query repository using Specification + Pageable
        Page<com.ainigma100.departmentapi.entity.Department> recordsFromDb =
                departmentRepository.findAll(spec, pageable);

        // Map entity list to DTO list
        List<DepartmentDTO> result = departmentMapper.departmentToDepartmentDto(recordsFromDb.getContent());

        // Return a page preserving the paging metadata
        return new PageImpl<>(result, pageable, recordsFromDb.getTotalElements());
    }

    @Override
    public DepartmentDTO getDepartmentById(Long id) {


        Department recordFromDB = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department with id '" + id + "' not found"));

        return departmentMapper.departmentToDepartmentDto(recordFromDB);
    }

    @Override
    public DepartmentDTO updateDepartment(DepartmentDTO departmentDTO, Long id) {


        Department recordFromDB = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department with id '" + id + "' not found"));

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
                .orElseThrow(() -> new EntityNotFoundException("Department with id '" + id + "' not found"));

        departmentRepository.delete(recordFromDB);

    }
}
