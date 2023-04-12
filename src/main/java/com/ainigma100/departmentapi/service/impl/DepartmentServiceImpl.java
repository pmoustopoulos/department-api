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


    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {

        Department recordFromDB = departmentRepository.findByDepartmentCode(departmentDTO.getDepartmentCode());

        if (recordFromDB != null) {
            throw new ResourceAlreadyExistException("Department", "departmentCode", departmentDTO.getDepartmentCode());
        }

        Department recordToBeSaved = departmentMapper.departmentDtoToDepartment(departmentDTO);

        Department savedRecord = departmentRepository.save(recordToBeSaved);

        DepartmentDTO result = departmentMapper.departmentToDepartmentDto(savedRecord);

        return result;
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

        return new PageImpl<>(result, pageable, result.size());
    }

    @Override
    public DepartmentDTO getDepartmentById(Long id) {


        Department recordFromDB = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        DepartmentDTO result = departmentMapper.departmentToDepartmentDto(recordFromDB);

        return result;
    }

    @Override
    public DepartmentDTO updateDepartment(DepartmentDTO departmentDTO, Long id) {


        Department recordFromDB = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        // just to be safe that the object does not have another id
        departmentDTO.setId(id);


        Department recordToBeSaved = departmentMapper.departmentDtoToDepartment(departmentDTO);
        // I had to set again the employees otherwise I would lose the reference
        recordToBeSaved.setEmployees(recordFromDB.getEmployees());

        Department savedRecord = departmentRepository.save(recordToBeSaved);

        DepartmentDTO result = departmentMapper.departmentToDepartmentDto(savedRecord);

        return result;
    }

    @Override
    public void deleteDepartment(Long id) {

        Department recordFromDB = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        departmentRepository.delete(recordFromDB);

    }
}
