package com.ainigma100.departmentapi.service.impl;

import com.ainigma100.departmentapi.dto.EmployeeDTO;
import com.ainigma100.departmentapi.dto.EmployeeSearchCriteriaDTO;
import com.ainigma100.departmentapi.entity.Department;
import com.ainigma100.departmentapi.entity.Employee;
import com.ainigma100.departmentapi.exception.BusinessLogicException;
import com.ainigma100.departmentapi.exception.ResourceAlreadyExistException;
import com.ainigma100.departmentapi.exception.ResourceNotFoundException;
import com.ainigma100.departmentapi.mapper.EmployeeMapper;
import com.ainigma100.departmentapi.repository.DepartmentRepository;
import com.ainigma100.departmentapi.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.*;

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
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    // @InjectMocks creates the mock object of the class and injects the mocks
    // that are marked with the annotations @Mock into it.
    @InjectMocks
    private EmployeeServiceImpl employeeService;


    private Employee employee;
    private EmployeeDTO employeeDTO;
    private Employee employee2;
    private EmployeeDTO employeeDTO2;
    private Department department;
    private Department department2;
    private EmployeeSearchCriteriaDTO employeeSearchCriteria;

    private Employee updatedEmployee;
    private EmployeeDTO updatedEmployeeDTO;


    @BeforeEach
    void setUp() {

        department = new Department();
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

        employee2 = new Employee();
        employee2.setId("emp02");
        employee2.setFirstName("Luffy");
        employee2.setLastName("Monkey D.");
        employee2.setEmail("mluffy@gmail.com");
        employee2.setSalary(BigDecimal.valueOf(50_000_000));
        employee2.setDepartment(department);

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

        department2 = new Department();
        department2.setId(2L);
        department2.setDepartmentCode("ABC");
        department2.setDepartmentName("Department 1");
        department2.setDepartmentDescription("Description 1");
        department2.setEmployees(new HashSet<>(Collections.singleton(employee2)));

        employeeSearchCriteria = new EmployeeSearchCriteriaDTO();
        employeeSearchCriteria.setPage(0);
        employeeSearchCriteria.setSize(10);
        employeeSearchCriteria.setFirstName("John");

        updatedEmployee = new Employee();
        updatedEmployee.setId("emp01");
        updatedEmployee.setFirstName("Marco");
        updatedEmployee.setLastName("Polo");
        updatedEmployee.setEmail("mpolo@gmail.com");
        updatedEmployee.setSalary(BigDecimal.valueOf(8_000_000));

        updatedEmployeeDTO = new EmployeeDTO();
        updatedEmployeeDTO.setId("emp01");
        updatedEmployeeDTO.setFirstName("Marco");
        updatedEmployeeDTO.setLastName("Polo");
        updatedEmployeeDTO.setEmail("mpolo@gmail.com");
        updatedEmployeeDTO.setSalary(BigDecimal.valueOf(8_000_000));

    }

    @Test
    @DisplayName("Given a valid department ID and employee DTO, when creating an employee, then return the employee DTO")
    void givenValidDepartmentIdAndEmployeeDTO_whenCreateEmployee_thenReturnEmployeeDTO() {

        // given - precondition or setup
        given(employeeRepository.findByEmail(employeeDTO.getEmail())).willReturn(null);
        given(employeeMapper.employeeDtoToEmployee(employeeDTO)).willReturn(employee);
        given(departmentRepository.findById(department.getId())).willReturn(Optional.of(department));
        given(employeeRepository.save(employee)).willReturn(employee);
        given(employeeMapper.employeeToEmployeeDto(employee)).willReturn(employeeDTO);

        // when - action or behavior that we are going to test
        EmployeeDTO result = employeeService.createEmployee(department.getId(), employeeDTO);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(employeeDTO.getFirstName());
        assertThat(result.getLastName()).isEqualTo(employeeDTO.getLastName());
        assertThat(result.getEmail()).isEqualTo(employeeDTO.getEmail());
        assertThat(result.getSalary()).isEqualByComparingTo(employeeDTO.getSalary());

        verify(employeeRepository, times(1)).findByEmail(employeeDTO.getEmail());
        verify(employeeMapper, times(1)).employeeDtoToEmployee(employeeDTO);
        verify(departmentRepository, times(1)).findById(department.getId());
        verify(employeeRepository, times(1)).save(employee);
        verify(employeeMapper, times(1)).employeeToEmployeeDto(employee);
    }

    @Test
    @DisplayName("Given an existing employee email, when creating an employee, then throw ResourceAlreadyExistException")
    void givenExistingEmployeeEmail_whenCreateEmployee_thenThrowResourceAlreadyExistException() {

        // given - precondition or setup
        given(employeeRepository.findByEmail(employeeDTO.getEmail())).willReturn(employee);

        // when/then - verify that the ResourceAlreadyExistException is thrown
        assertThatThrownBy(() -> employeeService.createEmployee(department.getId(), employeeDTO))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessageContaining("Resource Employee with email : '" + employeeDTO.getEmail() + "' already exist");

        verify(employeeRepository, times(1)).findByEmail(employeeDTO.getEmail());
        verify(employeeMapper, never()).employeeDtoToEmployee(any(EmployeeDTO.class));
        verify(departmentRepository, never()).findById(any(Long.class));
        verify(employeeRepository, never()).save(any(Employee.class));
        verify(employeeMapper, never()).employeeToEmployeeDto(any(Employee.class));
    }


    @Test
    @DisplayName("Given an employee search criteria DTO, when getting all employees using pagination, then return employee DTO page")
    void givenEmployeeSearchCriteriaDTO_whenGetAllEmployeesUsingPagination_thenReturnEmployeeDTOPage() {

        // given - precondition or setup
        List<Employee> employeeList = Arrays.asList(employee, employee2);
        List<EmployeeDTO> employeeDTOList = Arrays.asList(employeeDTO, employeeDTO2);
        Page<Employee> pageFromDb = new PageImpl<>(employeeList);

        PageRequest pageRequest = PageRequest.of(employeeSearchCriteria.getPage(), employeeSearchCriteria.getSize());

        given(employeeRepository.getAllEmployeesUsingPagination(employeeSearchCriteria, pageRequest))
                .willReturn(pageFromDb);

        given(employeeMapper.employeeToEmployeeDto(pageFromDb.getContent()))
                .willReturn(employeeDTOList);


        // when - action or behaviour that we are going to test
        Page<EmployeeDTO> result = employeeService.getAllEmployeesUsingPagination(employeeSearchCriteria);

        // then - verify the output
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getId()).isEqualTo("emp01");
        assertThat(result.getContent().get(0).getFirstName()).isEqualTo("John");
        assertThat(result.getContent().get(1).getId()).isEqualTo("emp02");
        assertThat(result.getContent().get(1).getFirstName()).isEqualTo("Luffy");

        verify(employeeRepository, times(1)).getAllEmployeesUsingPagination(employeeSearchCriteria, pageRequest);
        verify(employeeMapper, times(1)).employeeToEmployeeDto(employeeList);
    }

    @Test
    @DisplayName("Given an employee search criteria DTO that does not match, when getting all employees using pagination, then return empty page")
    void givenEmployeeSearchCriteriaDTOThatDoesNotMatch_whenGetAllEmployeesUsingPagination_thenReturnEmptyPage() {

        // given - precondition or setup
        Page<Employee> pageFromDb = new PageImpl<>(Collections.emptyList());

        PageRequest pageRequest = PageRequest.of(employeeSearchCriteria.getPage(), employeeSearchCriteria.getSize());

        employeeSearchCriteria.setLastName("Non existent value");

        given(employeeRepository.getAllEmployeesUsingPagination(employeeSearchCriteria, pageRequest))
                .willReturn(pageFromDb);

        given(employeeMapper.employeeToEmployeeDto(pageFromDb.getContent()))
                .willReturn(Collections.emptyList());


        // when - action or behaviour that we are going to test
        Page<EmployeeDTO> result = employeeService.getAllEmployeesUsingPagination(employeeSearchCriteria);

        // then - verify the output
        assertThat(result.getContent()).isEmpty();

        verify(employeeRepository, times(1)).getAllEmployeesUsingPagination(employeeSearchCriteria, pageRequest);
        verify(employeeMapper, times(1)).employeeToEmployeeDto(Collections.emptyList());
    }

    @Test
    @DisplayName("Given an existing department id, when getting employees by department id, then return list of employee DTO")
    void givenDepartmentId_whenGetEmployeesByDepartmentId_thenReturnEmployeeList() {

        // given - precondition or setup
        List<Employee> employeeList = Arrays.asList(employee, employee2);
        List<EmployeeDTO> employeeDTOList = Arrays.asList(employeeDTO, employeeDTO2);

        given(employeeRepository.findByDepartmentId(department.getId())).willReturn(employeeList);
        given(employeeMapper.employeeToEmployeeDto(employeeList)).willReturn(employeeDTOList);

        // when - action or behaviour that we are going to test
        List<EmployeeDTO> result = employeeService.getEmployeesByDepartmentId(department.getId());

        // then - verify the output
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(result.get(0).getLastName()).isEqualTo(employee.getLastName());
        assertThat(result.get(0).getEmail()).isEqualTo(employee.getEmail());
        assertThat(result.get(0).getSalary()).isEqualByComparingTo(employee.getSalary());
        assertThat(result.get(1).getFirstName()).isEqualTo(employee2.getFirstName());
        assertThat(result.get(1).getLastName()).isEqualTo(employee2.getLastName());
        assertThat(result.get(1).getEmail()).isEqualTo(employee2.getEmail());
        assertThat(result.get(1).getSalary()).isEqualByComparingTo(employee2.getSalary());

        verify(employeeRepository, times(1)).findByDepartmentId(department.getId());
        verify(employeeMapper, times(1)).employeeToEmployeeDto(employeeList);
    }


    @Test
    @DisplayName("Given valid department id and employee id, when get employee by id, then return an employee DTO")
    void givenDepartmentIdAndEmployeeId_whenGetEmployeeById_thenReturnEmployeeDTO() {

        // given - precondition or setup
        given(departmentRepository.findById(department.getId())).willReturn(Optional.of(department));
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
        given(employeeMapper.employeeToEmployeeDto(employee)).willReturn(employeeDTO);

        // when - action or behaviour that we are going to test
        EmployeeDTO result = employeeService.getEmployeeById(department.getId(), employee.getId());

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(employee.getId());
        assertThat(result.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(result.getSalary()).isEqualByComparingTo(employee.getSalary());

        verify(departmentRepository, times(1)).findById(department.getId());
        verify(employeeRepository, times(1)).findById(employee.getId());
        verify(employeeMapper, times(1)).employeeToEmployeeDto(employee);
    }

    @Test
    @DisplayName("Given an invalid department id, when get employee by id, then throw ResourceNotFoundException")
    void givenInvalidDepartmentId_whenGetEmployeeById_thenThrowResourceNotFoundException() {

        // given - precondition or setup
        Long departmentId = 123L;
        given(departmentRepository.findById(departmentId)).willReturn(Optional.empty());

        // when / then - action or behavior that we are going to test
        assertThatThrownBy(() -> employeeService.getEmployeeById(departmentId, employee.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Department with id : '" + departmentId + "' not found");


        // then - verify the output
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(employeeRepository, never()).findById(any(String.class));
        verify(employeeMapper, never()).employeeToEmployeeDto(any(Employee.class));
    }

    @Test
    @DisplayName("Given valid department id and invalid employee id, when get employee by id, then throw ResourceNotFoundException")
    void givenValidDepartmentIdAndInvalidEmployeeId_whenGetEmployeeById_thenThrowResourceNotFoundException() {

        // given - precondition or setup
        String employeeId = "invalid id";
        given(departmentRepository.findById(department.getId())).willReturn(Optional.of(department));
        given(employeeRepository.findById(employeeId)).willReturn(Optional.empty());

        // when - action or behaviour that we are going to test
        assertThatThrownBy(() -> employeeService.getEmployeeById(department.getId(), employeeId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee with id : '" + employeeId + "' not found");

        // then - verify the output
        verify(departmentRepository, times(1)).findById(department.getId());
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeMapper, never()).employeeToEmployeeDto(any(Employee.class));
    }

    @Test
    @DisplayName("Given department id and employee id which do not have an association, when get employee by id, then throw BusinessLogicException")
    void givenDepartmentIdAndEmployeeIdNotAssociated_whenGetEmployeeById_thenThrowBusinessLogicException() {

        // given - precondition or setup
        given(departmentRepository.findById(department2.getId())).willReturn(Optional.of(department2));
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));

        // when - action or behaviour that we are going to test
        assertThatThrownBy(() -> employeeService.getEmployeeById(department2.getId(), employee.getId()))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessage("Employee does not belong to Department");

        verify(departmentRepository, times(1)).findById(department2.getId());
        verify(employeeRepository, times(1)).findById(employee.getId());
        verify(employeeMapper, never()).employeeToEmployeeDto(any(Employee.class));
    }


    @Test
    @DisplayName("Given valid inputs, when updating employee by ID, then return updated EmployeeDTO")
    void givenValidInputs_whenUpdateEmployeeById_thenThenReturnUpdatedEmployeeDTO() {

        // given - precondition or setup
        given(departmentRepository.findById(department.getId())).willReturn(Optional.of(department));
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
        given(employeeMapper.employeeDtoToEmployee(updatedEmployeeDTO)).willReturn(updatedEmployee);
        given(employeeRepository.save(updatedEmployee)).willReturn(updatedEmployee);
        given(employeeMapper.employeeToEmployeeDto(updatedEmployee)).willReturn(updatedEmployeeDTO);

        // when - action or behaviour that we are going to test
        EmployeeDTO result = employeeService.updateEmployeeById(department.getId(), employee.getId(), updatedEmployeeDTO);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(employee.getId());

        verify(departmentRepository, times(1)).findById(department.getId());
        verify(employeeRepository, times(1)).findById(employee.getId());
        verify(employeeMapper, times(1)).employeeDtoToEmployee(updatedEmployeeDTO);
        verify(employeeRepository, times(1)).save(updatedEmployee);
        verify(employeeMapper, times(1)).employeeToEmployeeDto(updatedEmployee);
    }

    @Test
    @DisplayName("Given invalid department id, when updating employee by ID, then throw ResourceNotFoundException")
    void givenInvalidDepartmentId_whenUpdateEmployeeById_thenThenThrowResourceNotFoundException() {

        // given - precondition or setup
        Long departmentId = 123L;
        given(departmentRepository.findById(departmentId)).willReturn(Optional.empty());


        // when/then - verify that ResourceNotFoundException is thrown
        assertThatThrownBy(() -> employeeService.updateEmployeeById(departmentId, employee.getId(), updatedEmployeeDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Department with id : '" + departmentId + "' not found");

        verify(departmentRepository, times(1)).findById(departmentId);
        verify(employeeRepository, never()).findById(any(String.class));
        verify(employeeMapper, never()).employeeDtoToEmployee(any(EmployeeDTO.class));
        verify(employeeRepository, never()).save(any(Employee.class));
        verify(employeeMapper, never()).employeeToEmployeeDto(any(Employee.class));
    }

    @Test
    @DisplayName("Given invalid employee id, when updating employee by ID, then throw ResourceNotFoundException")
    void givenInvalidEmployeeId_whenUpdateEmployeeById_thenThenThrowResourceNotFoundException() {

        // given - precondition or setup
        String employeeId = "invalid emp id";
        given(departmentRepository.findById(department.getId())).willReturn(Optional.of(department));
        given(employeeRepository.findById(employeeId)).willReturn(Optional.empty());


        // when/then - verify that ResourceNotFoundException is thrown
        assertThatThrownBy(() -> employeeService.updateEmployeeById(department.getId(), employeeId, updatedEmployeeDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Employee with id : '" + employeeId + "' not found");

        verify(departmentRepository, times(1)).findById(department.getId());
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeMapper, never()).employeeDtoToEmployee(any(EmployeeDTO.class));
        verify(employeeRepository, never()).save(any(Employee.class));
        verify(employeeMapper, never()).employeeToEmployeeDto(any(Employee.class));
    }

    @Test
    @DisplayName("Given valid department id and employee id which are not associated, when updating employee by ID, then throw BusinessLogicException")
    void givenValidInputsWhichAreNotAssociated_whenUpdateEmployeeById_thenThenThrowBusinessLogicException() {

        // given - precondition or setup
        given(departmentRepository.findById(department2.getId())).willReturn(Optional.of(department2));
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));


        // when/then - verify that ResourceNotFoundException is thrown
        assertThatThrownBy(() -> employeeService.updateEmployeeById(department2.getId(), employee.getId(), updatedEmployeeDTO))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessageContaining("Employee does not belong to Department");

        verify(departmentRepository, times(1)).findById(department2.getId());
        verify(employeeRepository, times(1)).findById(employee.getId());
        verify(employeeMapper, never()).employeeDtoToEmployee(any(EmployeeDTO.class));
        verify(employeeRepository, never()).save(any(Employee.class));
        verify(employeeMapper, never()).employeeToEmployeeDto(any(Employee.class));
    }


    @Test
    @DisplayName("Given a valid department ID and employee ID, when deleting an employee, then the employee is deleted")
    void givenValidDepartmentIdAndEmployeeId_whenDeleteEmployee_thenEmployeeIsDeleted() {

        // given - precondition or setup
        given(departmentRepository.findById(department.getId())).willReturn(Optional.of(department));
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));

        // when - action or behavior that we are going to test
        employeeService.deleteEmployee(department.getId(), employee.getId());

        // then - verify that employeeRepository.delete is called
        verify(employeeRepository, times(1)).delete(employee);
    }

    @Test
    @DisplayName("Given an invalid department ID, when deleting an employee, then throw ResourceNotFoundException")
    void givenInvalidDepartmentId_whenDeleteEmployee_thenThrowResourceNotFoundException() {

        // given - precondition or setup
        Long departmentId = 123L;

        given(departmentRepository.findById(departmentId)).willReturn(Optional.empty());

        // when/then - verify that ResourceNotFoundException is thrown
        assertThatThrownBy(() -> employeeService.deleteEmployee(departmentId, employee.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Department with id : '" + departmentId + "' not found");

        verify(employeeRepository, never()).findById(anyString());
        verify(employeeRepository, never()).delete(any(Employee.class));
    }

    @Test
    @DisplayName("Given an invalid employee ID, when deleting an employee, then throw ResourceNotFoundException")
    void givenInvalidEmployeeId_whenDeleteEmployee_thenThrowResourceNotFoundException() {

        // given - precondition or setup
        String employeeId = "456";

        given(departmentRepository.findById(department.getId())).willReturn(Optional.of(department));
        given(employeeRepository.findById(employeeId)).willReturn(Optional.empty());

        // when/then - verify that ResourceNotFoundException is thrown
        assertThatThrownBy(() -> employeeService.deleteEmployee(department.getId(), employeeId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Employee with id : '" + employeeId + "' not found");

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, never()).delete(any(Employee.class));
    }

    @Test
    @DisplayName("Given an employee not belonging to the department, when deleting the employee, then throw BusinessLogicException")
    void givenEmployeeNotBelongingToDepartment_whenDeleteEmployee_thenThrowBusinessLogicException() {

        // given
        given(departmentRepository.findById(department2.getId())).willReturn(Optional.of(department2));
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));

        // when/then
        assertThatThrownBy(() -> employeeService.deleteEmployee(department2.getId(), employee.getId()))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessageContaining("Employee does not belong to Department");


        verify(employeeRepository, never()).delete(any(Employee.class));
    }


    @Test
    @DisplayName("Given null department or employee department, when checking if employee belongs to department, then return false")
    void givenNullDepartmentOrEmployeeDepartment_whenEmployeeBelongsToDepartment_thenReturnFalse() {

        // given - precondition or setup
        employee.setDepartment(null);

        given(departmentRepository.findById(department.getId())).willReturn(Optional.of(department));
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));

        // when/then - verify that BusinessLogicException is thrown
        assertThatThrownBy(() -> employeeService.deleteEmployee(department.getId(), employee.getId()))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessageContaining("Employee does not belong to Department");

        verify(employeeRepository, never()).delete(any(Employee.class));
    }


}
