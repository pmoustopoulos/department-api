package com.ainigma100.departmentapi.controller;

import com.ainigma100.departmentapi.dto.DepartmentDTO;
import com.ainigma100.departmentapi.dto.DepartmentRequestDTO;
import com.ainigma100.departmentapi.dto.DepartmentSearchCriteriaDTO;
import com.ainigma100.departmentapi.enums.Status;
import com.ainigma100.departmentapi.mapper.DepartmentMapper;
import com.ainigma100.departmentapi.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/*
 * @WebMvcTest annotation will load all the components required
 * to test the Controller layer. It will not load the service or repository layer components
 */
@WebMvcTest(DepartmentController.class)
@Tag("unit")
@AutoConfigureMockMvc(addFilters = false) // Disable Spring Security filters
class DepartmentControllerTest {

    @MockBean
    private DepartmentService departmentService;

    @MockBean
    private DepartmentMapper departmentMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;



    private DepartmentDTO departmentDTO;
    private DepartmentDTO updatedDepartmentDTO;
    private DepartmentSearchCriteriaDTO departmentSearchCriteriaDTO;
    private DepartmentRequestDTO departmentRequestDTO;


    @BeforeEach
    void setUp() {

        departmentDTO = new DepartmentDTO();
        departmentDTO.setId(1L);
        departmentDTO.setDepartmentCode("ABC");
        departmentDTO.setDepartmentName("Department 1");
        departmentDTO.setDepartmentDescription("Description 1");

        updatedDepartmentDTO = new DepartmentDTO();
        updatedDepartmentDTO.setId(1L);
        updatedDepartmentDTO.setDepartmentCode("ABC");
        updatedDepartmentDTO.setDepartmentName("Updated Department");
        updatedDepartmentDTO.setDepartmentDescription("Updated Description");

        departmentSearchCriteriaDTO = new DepartmentSearchCriteriaDTO();
        departmentSearchCriteriaDTO.setDepartmentCode("ABC");
        departmentSearchCriteriaDTO.setPage(0);
        departmentSearchCriteriaDTO.setSize(10);

        departmentRequestDTO = new DepartmentRequestDTO();
        departmentRequestDTO.setDepartmentCode("ABC");
        departmentRequestDTO.setDepartmentName("Department 1");
        departmentRequestDTO.setDepartmentDescription("Description 1");
    }


    @Test
    void givenDepartmentRequestDTO_whenCreateDepartment_thenReturnDepartmentDTO() throws Exception {

        // given - precondition or setup
        given(departmentMapper.departmentRequestDTOToDepartmentDTO(any(DepartmentRequestDTO.class)))
                .willReturn(departmentDTO);
        given(departmentService.createDepartment(any(DepartmentDTO.class)))
                .willReturn(departmentDTO);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departmentRequestDTO)));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isCreated())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.departmentCode", is(departmentRequestDTO.getDepartmentCode())))
                .andExpect(jsonPath("$.results.departmentName", is(departmentRequestDTO.getDepartmentName())))
                .andExpect(jsonPath("$.results.departmentDescription", is(departmentRequestDTO.getDepartmentDescription())));

    }


    @Test
    void givenDepartmentSearchCriteriaDTO_whenGetAllDepartmentsUsingPagination_thenReturnDepartmentDTOPage() throws Exception {

        // given - precondition or setup
        List<DepartmentDTO> departmentDTOList = Collections.singletonList(departmentDTO);
        Page<DepartmentDTO> departmentDTOPage = new PageImpl<>(departmentDTOList);
        given(departmentService.getAllDepartmentsUsingPagination(any(DepartmentSearchCriteriaDTO.class)))
                .willReturn(departmentDTOPage);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/departments/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departmentSearchCriteriaDTO)));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.content.size()", is(departmentDTOList.size())))
                .andExpect(jsonPath("$.results.content[0].departmentCode", is(departmentDTOList.get(0).getDepartmentCode())))
                .andExpect(jsonPath("$.results.content[0].departmentName", is(departmentDTOList.get(0).getDepartmentName())))
                .andExpect(jsonPath("$.results.content[0].departmentDescription", is(departmentDTOList.get(0).getDepartmentDescription())));

    }


    @Test
    void givenDepartmentId_whenGetDepartmentById_thenReturnDepartmentDTO() throws Exception {

        // given - precondition or setup
        given(departmentService.getDepartmentById(any(Long.class))).willReturn(departmentDTO);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/departments/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.departmentCode", is(departmentDTO.getDepartmentCode())))
                .andExpect(jsonPath("$.results.departmentName", is(departmentDTO.getDepartmentName())))
                .andExpect(jsonPath("$.results.departmentDescription", is(departmentDTO.getDepartmentDescription())));

    }

    @Test
    void givenDepartmentRequestDTOAndDepartmentId_whenUpdateDepartment_thenReturnDepartmentDTO() throws Exception {

        // given - precondition or setup
        given(departmentMapper.departmentRequestDTOToDepartmentDTO(any(DepartmentRequestDTO.class)))
                .willReturn(departmentDTO);
        given(departmentService.updateDepartment(any(DepartmentDTO.class), any(Long.class)))
                .willReturn(updatedDepartmentDTO);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/departments/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departmentRequestDTO)));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.departmentCode", is(updatedDepartmentDTO.getDepartmentCode())))
                .andExpect(jsonPath("$.results.departmentName", is(updatedDepartmentDTO.getDepartmentName())))
                .andExpect(jsonPath("$.results.departmentDescription", is(updatedDepartmentDTO.getDepartmentDescription())));

    }

    @Test
    void givenDepartmentId_whenDeleteDepartment_thenReturnString() throws Exception {

        // given - precondition or setup
        willDoNothing().given(departmentService).deleteDepartment(any(Long.class));


        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/v1/departments/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())));

    }

}
