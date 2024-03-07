package com.ainigma100.departmentapi.integration;

import com.ainigma100.departmentapi.dto.EmployeeRequestDTO;
import com.ainigma100.departmentapi.dto.EmployeeSearchCriteriaDTO;
import com.ainigma100.departmentapi.entity.Department;
import com.ainigma100.departmentapi.entity.Employee;
import com.ainigma100.departmentapi.enums.Status;
import com.ainigma100.departmentapi.repository.DepartmentRepository;
import com.ainigma100.departmentapi.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// Use random port for integration testing. the server will start on a random port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("testcontainers")
@Tag("integration")
class EmployeeControllerIntegrationTest extends AbstractContainerBaseTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;


    @BeforeEach
    void setUp() {

        employeeRepository.deleteAll();
        departmentRepository.deleteAll();

    }




    @Test
    @DisplayName("Add a new employee to the specific department")
    void givenDepartmentIdAndEmployeeRequestDTO_whenCreateEmployee_thenReturnEmployeeDTO() throws Exception {

        // given - precondition or setup
        Department department = new Department();
        department.setDepartmentCode("ABC");
        department.setDepartmentName("Department 1");
        department.setDepartmentDescription("Description 1");

        departmentRepository.save(department);

        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO();
        employeeRequestDTO.setFirstName("John");
        employeeRequestDTO.setLastName("Wick");
        employeeRequestDTO.setEmail("jwick@gmail.com");
        employeeRequestDTO.setSalary(BigDecimal.valueOf(40_000_000));

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/departments/{departmentId}/employees", department.getId())
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
        Department department = new Department();
        department.setDepartmentCode("ABC");
        department.setDepartmentName("Department 1");
        department.setDepartmentDescription("Description 1");

        departmentRepository.save(department);

        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Wick");
        employee.setEmail("jwick@gmail.com");
        employee.setSalary(BigDecimal.valueOf(40_000_000));
        employee.setDepartment(department);

        Employee employee2 = new Employee();
        employee2.setFirstName("Luffy");
        employee2.setLastName("Monkey D.");
        employee2.setEmail("mluffy@gmail.com");
        employee2.setSalary(BigDecimal.valueOf(50_000_000));
        employee2.setDepartment(department);


        List<Employee> employeeList = Arrays.asList(employee, employee2);
        employeeRepository.saveAll(employeeList);

        EmployeeSearchCriteriaDTO employeeSearchCriteria = new EmployeeSearchCriteriaDTO();
        employeeSearchCriteria.setPage(0);
        employeeSearchCriteria.setSize(10);
        employeeSearchCriteria.setEmail("gmail.com");

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
                .andExpect(jsonPath("$.results.content.size()", is(employeeList.size())));
    }

    @Test
    @DisplayName("Find all employees belonging to the specific department")
    void givenDepartmentId_whenGetEmployeesByDepartmentId_thenReturnEmployeeDTOList() throws Exception {

        // given - precondition or setup
        Department department = new Department();
        department.setDepartmentCode("ABC");
        department.setDepartmentName("Department 1");
        department.setDepartmentDescription("Description 1");

        departmentRepository.save(department);

        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Wick");
        employee.setEmail("jwick@gmail.com");
        employee.setSalary(BigDecimal.valueOf(40_000_000));
        employee.setDepartment(department);

        Employee employee2 = new Employee();
        employee2.setFirstName("Luffy");
        employee2.setLastName("Monkey D.");
        employee2.setEmail("mluffy@gmail.com");
        employee2.setSalary(BigDecimal.valueOf(50_000_000));
        employee2.setDepartment(department);


        List<Employee> employeeList = Arrays.asList(employee, employee2);
        employeeRepository.saveAll(employeeList);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/departments/{departmentId}/employees", department.getId()));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.size()", is(employeeList.size())));
    }

    @Test
    @DisplayName("Find employee by ID belonging to the specific department")
    void givenDepartmentIdAndEmployeeId_whenGetEmployeeById_thenReturnEmployeeDTO() throws Exception {

        // given - precondition or setup
        Department department = new Department();
        department.setDepartmentCode("ABC");
        department.setDepartmentName("Department 1");
        department.setDepartmentDescription("Description 1");

        departmentRepository.save(department);

        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Wick");
        employee.setEmail("jwick@gmail.com");
        employee.setSalary(BigDecimal.valueOf(10));
        employee.setDepartment(department);

        employeeRepository.save(employee);



        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/departments/{departmentId}/employees/{employeeId}",
                department.getId(), employee.getId()));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.results.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.results.email", is(employee.getEmail())))
                .andExpect(jsonPath("$.results.salary", is(10.00)));
    }

    @Test
    @DisplayName("Update an existing employee belonging to the specific department")
    void givenDepartmentIdEmployeeIdAndEmployeeRequestDTO_whenUpdateEmployeeById_thenReturnUpdatedEmployeeDTO() throws Exception {

        // given - precondition or setup
        Department department = new Department();
        department.setDepartmentCode("ABC");
        department.setDepartmentName("Department 1");
        department.setDepartmentDescription("Description 1");

        departmentRepository.save(department);

        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Wick");
        employee.setEmail("jwick@gmail.com");
        employee.setSalary(BigDecimal.valueOf(40_000_000));
        employee.setDepartment(department);

        employeeRepository.save(employee);

        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO();
        employeeRequestDTO.setFirstName("Marco");
        employeeRequestDTO.setLastName("Wick");
        employeeRequestDTO.setEmail("mwick@gmail.com");
        employeeRequestDTO.setSalary(BigDecimal.valueOf(8_000_000));

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/departments/{departmentId}/employees/{employeeId}",
                department.getId(), employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequestDTO)));

        // then - verify the output
        response.andDo(print())
                // Verify the status code
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.firstName", is(employeeRequestDTO.getFirstName())))
                .andExpect(jsonPath("$.results.lastName", is(employeeRequestDTO.getLastName())))
                .andExpect(jsonPath("$.results.email", is(employeeRequestDTO.getEmail())))
                .andExpect(jsonPath("$.results.salary", is(8_000_000)));
    }


    @Test
    @DisplayName("Delete an employee belonging to the specific department")
    void givenDepartmentIdAndEmployeeId_whenDeleteEmployee_thenReturnString() throws Exception {

        // given - precondition or setup
        Department department = new Department();
        department.setDepartmentCode("ABC");
        department.setDepartmentName("Department 1");
        department.setDepartmentDescription("Description 1");

        departmentRepository.save(department);

        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Wick");
        employee.setEmail("jwick@gmail.com");
        employee.setSalary(BigDecimal.valueOf(40_000_000));
        employee.setDepartment(department);

        employeeRepository.save(employee);


        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/v1/departments/{departmentId}/employees/{employeeId}",
                department.getId(), employee.getId()));

        // then - verify the output
        response.andDo(print())
                // Verify the status code
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())));
    }


    @Test
    @DisplayName("Get an employee along with the department using the employee email")
    void givenEmployeeEmail_whenGetEmployeeAndDepartmentByEmployeeEmail_thenReturnEmployeeAndDepartment() throws Exception {

        // given - precondition or setup
        Department department = new Department();
        department.setDepartmentCode("ABC");
        department.setDepartmentName("Department 1");
        department.setDepartmentDescription("Description 1");

        departmentRepository.save(department);

        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Wick");
        employee.setEmail("jwick@gmail.com");
        employee.setSalary(BigDecimal.valueOf(10));
        employee.setDepartment(department);

        employeeRepository.save(employee);



        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "jwick@gmail.com"));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.id", is(not(nullValue()))))
                .andExpect(jsonPath("$.results.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.results.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.results.email", is(employee.getEmail())))
                .andExpect(jsonPath("$.results.salary", is(10.00)))
                .andExpect(jsonPath("$.results.department.id", is(not(nullValue()))))
                .andExpect(jsonPath("$.results.department.departmentCode", is(department.getDepartmentCode())))
                .andExpect(jsonPath("$.results.department.departmentName", is(department.getDepartmentName())))
                .andExpect(jsonPath("$.results.department.departmentDescription", is(department.getDepartmentDescription())));

    }


}
