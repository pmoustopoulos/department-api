package com.ainigma100.departmentapi.service.impl;

import com.ainigma100.departmentapi.dto.DepartmentDTO;
import com.ainigma100.departmentapi.dto.DepartmentSearchCriteriaDTO;
import com.ainigma100.departmentapi.entity.Department;
import com.ainigma100.departmentapi.exception.ResourceAlreadyExistException;
import com.ainigma100.departmentapi.exception.ResourceNotFoundException;
import com.ainigma100.departmentapi.mapper.DepartmentMapper;
import com.ainigma100.departmentapi.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/*
 * @ExtendWith(MockitoExtension.class) informs Mockito that we are using
 * mockito annotations to mock the dependencies
 */
@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;


    // @InjectMocks creates the mock object of the class and injects the mocks
    // that are marked with the annotations @Mock into it.
    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;
    private DepartmentDTO departmentDTO;
    private Department updatedDepartment;
    private DepartmentDTO updatedDepartmentDTO;
    private DepartmentSearchCriteriaDTO departmentSearchCriteriaDTO;


    @BeforeEach
    void setUp() {

        department = new Department();
        department.setId(1L);
        department.setDepartmentCode("ABC");
        department.setDepartmentName("Department 1");
        department.setDepartmentDescription("Description 1");

        departmentDTO = new DepartmentDTO();
        departmentDTO.setId(1L);
        departmentDTO.setDepartmentCode("ABC");
        departmentDTO.setDepartmentName("Department 1");
        departmentDTO.setDepartmentDescription("Description 1");

        updatedDepartment = new Department();
        updatedDepartment.setId(1L);
        updatedDepartment.setDepartmentCode("ABC");
        updatedDepartment.setDepartmentName("Updated Department");
        updatedDepartment.setDepartmentDescription("Updated Description");

        updatedDepartmentDTO = new DepartmentDTO();
        updatedDepartmentDTO.setId(1L);
        updatedDepartmentDTO.setDepartmentCode("ABC");
        updatedDepartmentDTO.setDepartmentName("Updated Department");
        updatedDepartmentDTO.setDepartmentDescription("Updated Description");

        departmentSearchCriteriaDTO = new DepartmentSearchCriteriaDTO();
        departmentSearchCriteriaDTO.setDepartmentCode("ABC");
        departmentSearchCriteriaDTO.setPage(0);
        departmentSearchCriteriaDTO.setSize(10);

    }

    @Test
    void givenDepartmentDTO_whenCreateDepartment_thenReturnDepartmentDTO() {

        // given - precondition or setup
        String departmentCode = "ABC";
        given(departmentRepository.findByDepartmentCode(departmentCode)).willReturn(null);
        given(departmentMapper.departmentDtoToDepartment(departmentDTO)).willReturn(department);
        given(departmentRepository.save(department)).willReturn(department);
        given(departmentMapper.departmentToDepartmentDto(department)).willReturn(departmentDTO);

        // when - action or behaviour that we are going to test
        DepartmentDTO result = departmentService.createDepartment(departmentDTO);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getDepartmentCode()).isEqualTo(department.getDepartmentCode());
        assertThat(result.getDepartmentName()).isEqualTo(department.getDepartmentName());
        assertThat(result.getDepartmentDescription()).isEqualTo(department.getDepartmentDescription());

        verify(departmentRepository, times(1)).findByDepartmentCode(departmentCode);
        verify(departmentMapper, times(1)).departmentDtoToDepartment(departmentDTO);
        verify(departmentRepository, times(1)).save(department);
        verify(departmentMapper, times(1)).departmentToDepartmentDto(department);
    }

    @Test
    void givenExistingDepartmentDTO_whenCreateDepartment_thenThrowResourceAlreadyExistException() {

        // given - precondition or setup
        String departmentCode = "ABC";
        given(departmentRepository.findByDepartmentCode(departmentCode)).willReturn(department);

        // when / then
        assertThatThrownBy(() -> departmentService.createDepartment(departmentDTO))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessageContaining("Resource Department with departmentCode : '" + departmentCode + "' already exist");

        verify(departmentRepository, times(1)).findByDepartmentCode(departmentCode);
        verify(departmentMapper, never()).departmentDtoToDepartment(any(DepartmentDTO.class));
        verify(departmentRepository, never()).save(any());
        verify(departmentMapper, never()).departmentToDepartmentDto(any(Department.class));
    }

    @Test
    void givenDepartmentSearchCriteriaDTO_whenGetAllDepartmentsUsingPagination_thenReturnDepartmentDTOPage() {

        // given - precondition or setup
        departmentRepository.save(department);

        List<Department> departments = Collections.singletonList(department);
        Page<Department> departmentPage = new PageImpl<>(departments);

        given(departmentRepository.getAllDepartmentsUsingPagination(eq(departmentSearchCriteriaDTO), any(Pageable.class)))
                .willReturn(departmentPage);
        given(departmentMapper.departmentToDepartmentDto(departmentPage.getContent()))
                .willReturn(Collections.singletonList(departmentDTO));

        // when - action or behaviour that we are going to test
        Page<DepartmentDTO> result = departmentService.getAllDepartmentsUsingPagination(departmentSearchCriteriaDTO);

        // then - verify the output
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getDepartmentCode()).isEqualTo(department.getDepartmentCode());
        assertThat(result.getContent().get(0).getDepartmentName()).isEqualTo(department.getDepartmentName());

    }

    @Test
    void givenValidDepartmentId_whenGetDepartmentById_thenReturnDepartmentDTO() {

        // given - precondition or setup
        Long departmentId = 123L;
        given(departmentRepository.findById(departmentId)).willReturn(Optional.of(department));
        given(departmentMapper.departmentToDepartmentDto(department)).willReturn(departmentDTO);

        // when - action or behavior that we are going to test
        DepartmentDTO result = departmentService.getDepartmentById(departmentId);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(departmentDTO);

        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentMapper, times(1)).departmentToDepartmentDto(department);
    }

    @Test
    void givenInvalidDepartmentId_whenGetDepartmentById_thenThrowResourceNotFoundException() {
        
        // given - precondition or setup
        Long departmentId = 123L;
        given(departmentRepository.findById(departmentId)).willReturn(Optional.empty());

        // when/then - action or behavior that we are going to test
        assertThatThrownBy(() -> departmentService.getDepartmentById(departmentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Department with id : '" + departmentId + "' not found");

        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentMapper, never()).departmentToDepartmentDto(any(Department.class));
    }

    @Test
    void givenValidDepartmentDTOAndId_whenUpdateDepartment_thenReturnUpdatedDepartmentDTO() {

        // given - precondition or setup
        given(departmentRepository.findById(department.getId())).willReturn(Optional.of(department));
        given(departmentMapper.departmentDtoToDepartment(departmentDTO)).willReturn(updatedDepartment);
        given(departmentRepository.save(updatedDepartment)).willReturn(updatedDepartment);
        given(departmentMapper.departmentToDepartmentDto(updatedDepartment)).willReturn(updatedDepartmentDTO);

        // when - action or behavior that we are going to test
        DepartmentDTO result = departmentService.updateDepartment(departmentDTO, department.getId());

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(updatedDepartment.getId());
        assertThat(result.getDepartmentCode()).isEqualTo(updatedDepartment.getDepartmentCode());
        assertThat(result.getDepartmentName()).isEqualTo(updatedDepartment.getDepartmentName());
        assertThat(result.getDepartmentDescription()).isEqualTo(updatedDepartment.getDepartmentDescription());

        verify(departmentRepository, times(1)).findById(department.getId());
        verify(departmentMapper, times(1)).departmentDtoToDepartment(departmentDTO);
        verify(departmentRepository, times(1)).save(updatedDepartment);
        verify(departmentMapper, times(1)).departmentToDepartmentDto(updatedDepartment);
    }

    @Test
    void givenInvalidDepartmentDTOOrId_whenUpdateDepartment_thenThrowResourceNotFoundException() {

        // given - precondition or setup
        Long departmentId = 123L;
        given(departmentRepository.findById(departmentId)).willReturn(Optional.empty());

        // when / then - action or behavior that we are going to test
        assertThatThrownBy(() -> departmentService.updateDepartment(updatedDepartmentDTO, departmentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Department with id : '" + departmentId + "' not found");

        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentMapper, never()).departmentDtoToDepartment(any(DepartmentDTO.class));
        verify(departmentRepository, never()).save(any(Department.class));
        verify(departmentMapper, never()).departmentToDepartmentDto(any(Department.class));
    }

    @Test
    void givenExistingDepartmentId_whenDeleteDepartment_thenDepartmentDeleted() {

        // given - precondition or setup
        given(departmentRepository.findById(department.getId())).willReturn(Optional.of(department));

        // when - action or behavior that we are going to test
        departmentService.deleteDepartment(department.getId());

        // then - verify the output
        verify(departmentRepository, times(1)).findById(department.getId());
        verify(departmentRepository, times(1)).delete(department);
    }

    @Test
    void givenNonexistentDepartmentId_whenDeleteDepartment_thenThrowResourceNotFoundException() {

        // given - precondition or setup
        Long departmentId = 123L;

        // Mocking the behavior of departmentRepository
        given(departmentRepository.findById(departmentId)).willReturn(Optional.empty());

        // when/then - verify that the ResourceNotFoundException is thrown
        assertThatThrownBy(() -> departmentService.deleteDepartment(departmentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Department with id : '" + departmentId + "' not found");

        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentRepository, never()).delete(any(Department.class));
    }


}
