package com.ainigma100.departmentapi.repository;

import com.ainigma100.departmentapi.dto.EmployeeSearchCriteriaDTO;
import com.ainigma100.departmentapi.entity.Department;
import com.ainigma100.departmentapi.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * @DataJpaTest will automatically configure in-memory database for testing
 * and, it will not load annotated beans into the Application Context.
 * It will only load the repository class. Tests annotated with @DataJpaTest
 * are by default transactional and roll back at the end of each test.
 */
@DataJpaTest
@ActiveProfiles("test")
@Tag("unit")
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;


    private Employee employee1;
    private Employee employee2;
    private Department department;


    @BeforeEach
    void setUp() {

        department = new Department();
        department.setDepartmentCode("ABC");
        department.setDepartmentName("Department 1");
        department.setDepartmentDescription("Description 1");

        employee1 = new Employee();
        employee1.setFirstName("John");
        employee1.setLastName("Wick");
        employee1.setEmail("jwick@gmail.com");
        employee1.setSalary(BigDecimal.valueOf(40_000_000));
        employee1.setDepartment(department);

        employee2 = new Employee();
        employee2.setFirstName("Luffy");
        employee2.setLastName("Monkey D.");
        employee2.setEmail("mluffy@gmail.com");
        employee2.setSalary(BigDecimal.valueOf(50_000_000));
        employee2.setDepartment(department);

    }



    @Test
    void givenDepartmentId_whenFindByDepartmentId_thenReturnEmployeeList() {

        // given - precondition or setup
        departmentRepository.save(department);
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        // when - action or behaviour that we are going to test
        List<Employee> employeeList = employeeRepository.findByDepartmentId(department.getId());

        // then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).hasSize(2);
        assertThat(employeeList.get(0).getFirstName()).isEqualTo("John");
        assertThat(employeeList.get(0).getLastName()).isEqualTo("Wick");
        assertThat(employeeList.get(1).getFirstName()).isEqualTo("Luffy");
        assertThat(employeeList.get(1).getSalary()).isEqualByComparingTo(BigDecimal.valueOf(50_000_000));

    }

    @Test
    void givenEmployeeSearchCriteriaDTO_whenGetAllEmployeesUsingPagination_thenEmployeePage() {

        // given - precondition or setup
        departmentRepository.save(department);
        employeeRepository.save(employee1);

        EmployeeSearchCriteriaDTO searchCriteria = new EmployeeSearchCriteriaDTO();
        searchCriteria.setFirstName("John");

        PageRequest pageRequest = PageRequest.of(0, 10);

        // when - action or behaviour that we are going to test
        Page<Employee> employeePage = employeeRepository.getAllEmployeesUsingPagination(searchCriteria, pageRequest);

        // then - verify the output
        assertThat(employeePage).isNotNull();
        assertThat(employeePage.getContent()).hasSize(1);
        assertThat(employeePage.getContent().get(0).getFirstName()).isEqualTo("John");
        assertThat(employeePage.getContent().get(0).getEmail()).isEqualTo("jwick@gmail.com");
        assertThat(employeePage.getContent().get(0).getSalary()).isEqualByComparingTo(BigDecimal.valueOf(40_000_000));

    }

    @Test
    void givenEmail_whenFindByEmail_thenReturnEmployee() {

        // given - precondition or setup
        departmentRepository.save(department);
        employeeRepository.save(employee1);

        // when - action or behaviour that we are going to test
        Employee employeeFromDb = employeeRepository.findByEmail(employee1.getEmail());

        // then - verify the output
        assertThat(employeeFromDb).isNotNull();
        assertThat(employeeFromDb.getFirstName()).isEqualTo("John");
        assertThat(employeeFromDb.getLastName()).isEqualTo("Wick");
        assertThat(employeeFromDb.getEmail()).isEqualTo("jwick@gmail.com");
        assertThat(employeeFromDb.getSalary()).isEqualByComparingTo(BigDecimal.valueOf(40_000_000));
        assertThat(employeeFromDb.getDepartment()).isNotNull();

    }
}
