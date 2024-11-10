package com.ainigma100.departmentapi.integration;

import com.ainigma100.departmentapi.dto.DepartmentRequestDTO;
import com.ainigma100.departmentapi.dto.DepartmentSearchCriteriaDTO;
import com.ainigma100.departmentapi.entity.Department;
import com.ainigma100.departmentapi.enums.Status;
import com.ainigma100.departmentapi.filter.RateLimitingFilter;
import com.ainigma100.departmentapi.repository.DepartmentRepository;
import com.ainigma100.departmentapi.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// use random port for integration testing. the server will start on a random port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
@ActiveProfiles("testcontainers")
@Tag("integration")
class DepartmentControllerIntegrationTest extends AbstractContainerBaseTest {


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RateLimitingFilter rateLimitingFilter;

    @Value("${rate-limiting.max-requests}")
    private int maxRequests;


    @BeforeEach
    void setUp() {

        employeeRepository.deleteAll();
        departmentRepository.deleteAll();

    }

    @AfterEach
    void resetRateLimitBuckets() {
        rateLimitingFilter.clearBuckets();
    }


    @Test
    void givenDepartmentRequestDTO_whenCreateDepartment_thenReturnDepartmentDTO() throws Exception {

        // given - precondition or setup
        DepartmentRequestDTO departmentRequestDTO = new DepartmentRequestDTO();
        departmentRequestDTO.setDepartmentCode("ABC");
        departmentRequestDTO.setDepartmentName("Department 1");
        departmentRequestDTO.setDepartmentDescription("Description 1");

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
        Department department = new Department();
        department.setDepartmentCode("ABC");
        department.setDepartmentName("Department 1");
        department.setDepartmentDescription("Description 1");

        Department department2 = new Department();
        department2.setDepartmentCode("FIN");
        department2.setDepartmentName("Department 2");
        department2.setDepartmentDescription("Description 2");

        List<Department> departmentList = Arrays.asList(department, department2);
        departmentRepository.saveAll(departmentList);

        DepartmentSearchCriteriaDTO departmentSearchCriteriaDTO = new DepartmentSearchCriteriaDTO();
        departmentSearchCriteriaDTO.setDepartmentCode("ABC");
        departmentSearchCriteriaDTO.setPage(0);
        departmentSearchCriteriaDTO.setSize(10);


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
                .andExpect(jsonPath("$.results.content.size()", is(1)));

    }


    @Test
    void givenDepartmentId_whenGetDepartmentById_thenReturnDepartmentDTO() throws Exception {

        // given - precondition or setup
        Department department = new Department();
        department.setDepartmentCode("ABC");
        department.setDepartmentName("Department 1");
        department.setDepartmentDescription("Description 1");

        departmentRepository.save(department);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/departments/{id}", department.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.departmentCode", is(department.getDepartmentCode())))
                .andExpect(jsonPath("$.results.departmentName", is(department.getDepartmentName())))
                .andExpect(jsonPath("$.results.departmentDescription", is(department.getDepartmentDescription())));

    }

    @Test
    void givenDepartmentRequestDTOAndDepartmentId_whenUpdateDepartment_thenReturnDepartmentDTO() throws Exception {

        // given - precondition or setup
        Department department = new Department();
        department.setDepartmentCode("ABC");
        department.setDepartmentName("Department 1");
        department.setDepartmentDescription("Description 1");

        departmentRepository.save(department);

        DepartmentRequestDTO departmentRequestDTO = new DepartmentRequestDTO();
        departmentRequestDTO.setDepartmentCode("ABC");
        departmentRequestDTO.setDepartmentName("Updated Department 1");
        departmentRequestDTO.setDepartmentDescription("Updated Description 1");

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/departments/{id}", department.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departmentRequestDTO)));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.departmentCode", is(departmentRequestDTO.getDepartmentCode())))
                .andExpect(jsonPath("$.results.departmentName", is(departmentRequestDTO.getDepartmentName())))
                .andExpect(jsonPath("$.results.departmentDescription", is(departmentRequestDTO.getDepartmentDescription())));

    }

    @Test
    void givenDepartmentId_whenDeleteDepartment_thenReturnString() throws Exception {

        // given - precondition or setup
        Department department = new Department();
        department.setDepartmentCode("ABC");
        department.setDepartmentName("Department 1");
        department.setDepartmentDescription("Description 1");

        departmentRepository.save(department);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/v1/departments/{id}", department.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())));

    }


    @Test
    @DisplayName("Test rate limiting - should return 429 when limit exceeded")
    void givenDepartmentId_whenGetDepartmentByIdAndExceedingRateLimit_thenReturnTooManyRequests() throws Exception {

        // given - precondition or setup
        Department department = new Department();
        department.setDepartmentCode("ABC");
        department.setDepartmentName("Department 1");
        department.setDepartmentDescription("Description 1");

        departmentRepository.save(department);

        // Send requests up to the rate limit
        for (int i = 0; i < maxRequests; i++) {
            mockMvc.perform(get("/api/v1/departments/{id}", department.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        // The next request should exceed the rate limit
        ResultActions response = mockMvc.perform(get("/api/v1/departments/{id}", department.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output for rate limit exceeded
        response.andDo(print())
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$", is("Rate limit exceeded. Please try again later.")));
    }


}
