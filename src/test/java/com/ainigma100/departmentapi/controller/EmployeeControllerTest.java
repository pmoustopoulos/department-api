package com.ainigma100.departmentapi.controller;

import com.ainigma100.departmentapi.dto.EmployeeAndDepartmentDTO;
import com.ainigma100.departmentapi.dto.EmployeeDTO;
import com.ainigma100.departmentapi.dto.EmployeeRequestDTO;
import com.ainigma100.departmentapi.dto.EmployeeSearchCriteriaDTO;
import com.ainigma100.departmentapi.entity.Department;
import com.ainigma100.departmentapi.entity.Employee;
import com.ainigma100.departmentapi.enums.Status;
import com.ainigma100.departmentapi.filter.RateLimitingFilter;
import com.ainigma100.departmentapi.mapper.EmployeeMapper;
import com.ainigma100.departmentapi.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
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
@WebMvcTest(EmployeeController.class)
@Tag("unit")
class EmployeeControllerTest {

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private EmployeeMapper employeeMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RateLimitingFilter rateLimitingFilter;

    private Employee employee;
    private EmployeeDTO employeeDTO;
    private EmployeeDTO employeeDTO2;
    private EmployeeSearchCriteriaDTO employeeSearchCriteria;
    private EmployeeDTO updatedEmployeeDTO;
    private EmployeeRequestDTO employeeRequestDTO;
    private EmployeeAndDepartmentDTO employeeAndDepartmentDTO;


    @BeforeEach
    void setUp() {

        Department department = new Department();
        department.setId(1L);
        department.setDepartmentCode("ABC");
        department.setDepartmentName("Department 1");
        department.setDepartmentDescription("Description 1");
        department.setEmployees(new HashSet<>(Collections.singleton(employee)));

        employee = new Employee();
        employee.setId("emp01");
        employee.setFirstName("John");
        employee.setLastName("Wick");
        employee.setEmail("jwick@gmail.com");
        employee.setSalary(BigDecimal.valueOf(40_000_000));
        employee.setDepartment(department);

        employeeDTO = new EmployeeDTO();
        employeeDTO.setId("emp01");
        employeeDTO.setFirstName("John");
        employeeDTO.setLastName("Wick");
        employeeDTO.setEmail("jwick@gmail.com");
        employeeDTO.setSalary(BigDecimal.valueOf(40_000_000));

        employeeDTO2 = new EmployeeDTO();
        employeeDTO2.setId("emp02");
        employeeDTO2.setFirstName("Luffy");
        employeeDTO2.setLastName("Monkey D.");
        employeeDTO2.setEmail("mluffy@gmail.com");
        employeeDTO2.setSalary(BigDecimal.valueOf(50_000_000));

        employeeSearchCriteria = new EmployeeSearchCriteriaDTO();
        employeeSearchCriteria.setPage(0);
        employeeSearchCriteria.setSize(10);
        employeeSearchCriteria.setFirstName("John");

        updatedEmployeeDTO = new EmployeeDTO();
        updatedEmployeeDTO.setId("emp01");
        updatedEmployeeDTO.setFirstName("Marco");
        updatedEmployeeDTO.setLastName("Polo");
        updatedEmployeeDTO.setEmail("mpolo@gmail.com");
        updatedEmployeeDTO.setSalary(BigDecimal.valueOf(8_000_000));

        employeeRequestDTO = new EmployeeRequestDTO();
        employeeRequestDTO.setFirstName("John");
        employeeRequestDTO.setLastName("Wick");
        employeeRequestDTO.setEmail("jwick@gmail.com");
        employeeRequestDTO.setSalary(BigDecimal.valueOf(40_000_000));

        employeeAndDepartmentDTO = new EmployeeAndDepartmentDTO();
        employeeAndDepartmentDTO.setId("emp01");
        employeeAndDepartmentDTO.setFirstName("John");
        employeeAndDepartmentDTO.setLastName("Wick");
        employeeAndDepartmentDTO.setEmail("jwick@gmail.com");
        employeeAndDepartmentDTO.setSalary(BigDecimal.valueOf(40_000_000));
        employeeAndDepartmentDTO.setDepartment(new EmployeeAndDepartmentDTO.DepartmentDTO(
                2L,
                "DEP_ABC",
                "Department Name ABC",
                "Department Description ABC"
        ));

    }


    @AfterEach
    void resetRateLimitBuckets() {
        rateLimitingFilter.clearBuckets();
    }

    @Test
    @DisplayName("Add a new employee to the specific department")
    void givenDepartmentIdAndEmployeeRequestDTO_whenCreateEmployee_thenReturnEmployeeDTO() throws Exception {

        // given - precondition or setup
        given(employeeMapper.employeeRequestDTOToEmployeeDTO(any(EmployeeRequestDTO.class)))
                .willReturn(employeeDTO);
        given(employeeService.createEmployee(any(Long.class), any(EmployeeDTO.class)))
                .willReturn(employeeDTO);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/departments/{departmentId}/employees", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequestDTO)));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isCreated())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.firstName", is(employeeRequestDTO.getFirstName())))
                .andExpect(jsonPath("$.results.lastName", is(employeeRequestDTO.getLastName())))
                .andExpect(jsonPath("$.results.email", is(employeeRequestDTO.getEmail())))
                .andExpect(jsonPath("$.results.salary", is(40_000_000)));
    }


    @Test
    @DisplayName("Search all employees from all departments using pagination")
    void givenEmployeeSearchCriteriaDTO_whenGetAllEmployeesUsingPagination_thenReturnEmployeeDTOPage() throws Exception {

        // given - precondition or setup
        List<EmployeeDTO> employeeDTOList = Arrays.asList(employeeDTO, employeeDTO2);
        Page<EmployeeDTO> employeeDTOPage = new PageImpl<>(employeeDTOList);
        given(employeeService.getAllEmployeesUsingPagination(any(EmployeeSearchCriteriaDTO.class)))
                .willReturn(employeeDTOPage);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/employees/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeSearchCriteria)));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.content.size()", is(employeeDTOList.size())))
                .andExpect(jsonPath("$.results.content[0].firstName", is(employeeDTOList.get(0).getFirstName())))
                .andExpect(jsonPath("$.results.content[0].lastName", is(employeeDTOList.get(0).getLastName())))
                .andExpect(jsonPath("$.results.content[0].email", is(employeeDTOList.get(0).getEmail())))
                .andExpect(jsonPath("$.results.content[0].salary", is(40_000_000)));
    }

    @Test
    @DisplayName("Find all employees belonging to the specific department")
    void givenDepartmentId_whenGetEmployeesByDepartmentId_thenReturnEmployeeDTOList() throws Exception {

        // given - precondition or setup
        List<EmployeeDTO> employeeDTOList = Arrays.asList(employeeDTO, employeeDTO2);
        given(employeeService.getEmployeesByDepartmentId(any(Long.class)))
                .willReturn(employeeDTOList);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/departments/{departmentId}/employees", 1L));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.size()", is(employeeDTOList.size())));
    }

    @Test
    @DisplayName("Find employee by ID belonging to the specific department")
    void givenDepartmentIdAndEmployeeId_whenGetEmployeeById_thenReturnEmployeeDTO() throws Exception {

        // given - precondition or setup
        given(employeeService.getEmployeeById(any(Long.class), any(String.class)))
                .willReturn(employeeDTO);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/departments/{departmentId}/employees/{employeeId}", 1L, "emp01"));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.firstName", is(employeeDTO.getFirstName())))
                .andExpect(jsonPath("$.results.lastName", is(employeeDTO.getLastName())))
                .andExpect(jsonPath("$.results.email", is(employeeDTO.getEmail())))
                .andExpect(jsonPath("$.results.salary", is(40_000_000)));
    }

    @Test
    @DisplayName("Update an existing employee belonging to the specific department")
    void givenDepartmentIdEmployeeIdAndEmployeeRequestDTO_whenUpdateEmployeeById_thenReturnUpdatedEmployeeDTO() throws Exception {

        // given - precondition or setup
        given(employeeMapper.employeeRequestDTOToEmployeeDTO(any(EmployeeRequestDTO.class)))
                .willReturn(updatedEmployeeDTO);
        given(employeeService.updateEmployeeById(any(Long.class), any(String.class), any(EmployeeDTO.class)))
                .willReturn(updatedEmployeeDTO);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/departments/{departmentId}/employees/{employeeId}", 1L, "emp01")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequestDTO)));

        // then - verify the output
        response.andDo(print())
                // Verify the status code
                .andExpect(status().isOk())
                // Verify the returned value
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.firstName", is(updatedEmployeeDTO.getFirstName())))
                .andExpect(jsonPath("$.results.lastName", is(updatedEmployeeDTO.getLastName())))
                .andExpect(jsonPath("$.results.email", is(updatedEmployeeDTO.getEmail())))
                .andExpect(jsonPath("$.results.salary", is(8_000_000)));
    }


    @Test
    @DisplayName("Delete an employee belonging to the specific department")
    void givenDepartmentIdAndEmployeeId_whenDeleteEmployee_thenReturnString() throws Exception {

        // given - precondition or setup
        willDoNothing().given(employeeService).deleteEmployee(any(Long.class), any(String.class));

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/v1/departments/{departmentId}/employees/{employeeId}", 1L, "emp01"));

        // then - verify the output
        response.andDo(print())
                // Verify the status code
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("Get an employee along with the department using the employee email")
    void givenEmployeeEmail_whenGetEmployeeAndDepartmentByEmployeeEmail_thenReturnEmployeeAndDepartment() throws Exception {

        // given - precondition or setup
        given(employeeService.getEmployeeAndDepartmentByEmployeeEmail(any(String.class)))
                .willReturn(employeeAndDepartmentDTO);


        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "jwick@gmail.com"));

        // then - verify the output
        response.andDo(print())
                // Verify the status code
                .andExpect(status().isOk())
                // Verify the returned value
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.firstName", is(employeeAndDepartmentDTO.getFirstName())))
                .andExpect(jsonPath("$.results.lastName", is(employeeAndDepartmentDTO.getLastName())))
                .andExpect(jsonPath("$.results.email", is(employeeAndDepartmentDTO.getEmail())))
                .andExpect(jsonPath("$.results.salary", is(40_000_000)))
                .andExpect(jsonPath("$.results.department.id", is(2)))
                .andExpect(jsonPath("$.results.department.departmentCode", is(employeeAndDepartmentDTO.getDepartment().getDepartmentCode())))
                .andExpect(jsonPath("$.results.department.departmentName", is(employeeAndDepartmentDTO.getDepartment().getDepartmentName())))
                .andExpect(jsonPath("$.results.department.departmentDescription", is(employeeAndDepartmentDTO.getDepartment().getDepartmentDescription())));

    }

}
