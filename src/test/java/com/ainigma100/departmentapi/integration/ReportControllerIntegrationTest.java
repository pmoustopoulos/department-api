package com.ainigma100.departmentapi.integration;

import com.ainigma100.departmentapi.entity.Department;
import com.ainigma100.departmentapi.entity.Employee;
import com.ainigma100.departmentapi.repository.DepartmentRepository;
import com.ainigma100.departmentapi.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Use random port for integration testing. the server will start on a random port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("testcontainers")
class ReportControllerIntegrationTest extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private static List<Department> departmentList;
    private static List<Employee> employeeList;

    @BeforeAll
    public static void setupClass() {

        Department department = new Department();
        department.setDepartmentCode("ABC");
        department.setDepartmentName("Department 1");
        department.setDepartmentDescription("Description 1");

        Department department2 = new Department();
        department2.setDepartmentCode("FIN");
        department2.setDepartmentName("Department 2");
        department2.setDepartmentDescription("Description 2");

        departmentList = Arrays.asList(department, department2);


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


        employeeList = Arrays.asList(employee, employee2);

    }

    @BeforeEach
    public void setup() {
        departmentRepository.saveAll(departmentList);
        employeeRepository.saveAll(employeeList);
    }



    @Test
    @DisplayName("Generate an Excel report containing all the departments")
    void givenNoInput_whenGenerateDepartmentsExcelReport_thenReturnInputStreamResource() throws Exception {

        // given - precondition or setup

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/reports/excel/departments"));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Generate an Excel report containing all the employees")
    void givenNoInput_whenGenerateEmployeesExcelReport_thenReturnInputStreamResource() throws Exception {

        // given - precondition or setup

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/reports/excel/employees"));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Generate a PDF report containing all the departments along with all the employees")
    void givenNoInput_whenGeneratePdfFullReport_thenReturnInputStreamResource() throws Exception {

        // given - precondition or setup

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/reports/pdf/full-report"));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk());

    }



    @Test
    @DisplayName("Generate a zip file which contains two excel reports")
    void givenNoInput_whenGenerateAndZipReports_thenReturnInputStreamResource() throws Exception {

        // given - precondition or setup

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/reports/zip"));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Generate a multi-sheet Excel report containing departments and employees")
    void givenNoInput_whenGenerateMultiSheetExcelReport_thenReturnInputStreamResource() throws Exception {

        // given - precondition or setup

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/reports/multi-sheet-excel"));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk());

    }


}