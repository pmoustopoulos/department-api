package com.ainigma100.departmentapi.integration;

import com.ainigma100.departmentapi.entity.Department;
import com.ainigma100.departmentapi.entity.Employee;
import com.ainigma100.departmentapi.repository.DepartmentRepository;
import com.ainigma100.departmentapi.repository.EmployeeRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Use random port for integration testing. the server will start on a random port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
@Tag("integration")
class ReportControllerIntegrationTest extends AbstractContainerBaseTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	NumberFormat formatter ;

	private static List<Department> departmentList;
	private static List<Employee> employeeList;

	@BeforeEach
	public void setup() {

		formatter = new DecimalFormat("#");

		Department department = new Department();
		department.setDepartmentCode( "ABC" );
		department.setDepartmentName( "Department 1" );
		department.setDepartmentDescription( "Description 1" );

		Department department2 = new Department();
		department2.setDepartmentCode( "FIN" );
		department2.setDepartmentName( "Department 2" );
		department2.setDepartmentDescription( "Description 2" );

		departmentList = Arrays.asList( department, department2 );

		Employee employee = new Employee();
		employee.setFirstName( "John" );
		employee.setLastName( "Wick" );
		employee.setEmail( "jwick@gmail.com" );
		employee.setSalary( BigDecimal.valueOf( 40_000_000 ) );
		employee.setDepartment( department );

		Employee employee2 = new Employee();
		employee2.setFirstName( "Luffy" );
		employee2.setLastName( "Monkey D." );
		employee2.setEmail( "mluffy@gmail.com" );
		employee2.setSalary( BigDecimal.valueOf( 50_000_000 ) );
		employee2.setDepartment( department );

		employeeList = Arrays.asList( employee, employee2 );

		departmentRepository.deleteAll();
		employeeRepository.deleteAll();
	}

	@Test
	@DisplayName("Generate an Excel report containing all the departments")
	void givenNoInput_whenGenerateDepartmentsExcelReport_thenReturnInputStreamResource()
			throws Exception {

		// given - precondition or setup
		departmentRepository.saveAll( departmentList );
		employeeRepository.saveAll( employeeList );

		// when - action or behaviour that we are going to test
		ResultActions response = mockMvc.perform( get( "/api/v1/reports/excel/departments" ) );

		// then - verify the output
		response.andExpect( status().isOk() );

		InputStream is = new ByteArrayInputStream(
				response.andReturn().getResponse().getContentAsByteArray() );


		//Create Workbook instance holding reference to .xlsx file
		XSSFWorkbook workbook = new XSSFWorkbook( is );

		//Get first/desired sheet from the workbook
		XSSFSheet sheet = workbook.getSheetAt( 0 );

		//Iterate through each row one by one
		Iterator<Row> rowIterator = sheet.iterator();


		// Skip rows that do not contain info given that I know the template of the xlsx
		skipRows( rowIterator, 5 );

		for ( int i = 0; i < departmentList.size(); i++ ) {
			String departmentName = departmentList.get( i ).getDepartmentName();
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			cellIterator.next();
			// id | Department Code | Department Name | Department Description | Total employee |
			int count = 0;
			while ( cellIterator.hasNext() ) {
				Cell cell = cellIterator.next();
				//Check the cell type and format accordingly
				switch ( count ) {
				case 0:
					assertThat( cell.getStringCellValue() ).isEqualTo( departmentList.get( i ).getDepartmentCode() );
					break;
				case 1:
					assertThat( cell.getStringCellValue() ).isEqualTo( departmentList.get( i ).getDepartmentName() );
					break;
				case 2:
					assertThat( cell.getStringCellValue() ).isEqualTo( departmentList.get( i ).getDepartmentDescription() );
					break;
				case 3:
					Long employeeDepartmentCounter = employeeList.stream()
							.filter( e -> e.getDepartment()
									.getDepartmentName()
									.equals( departmentName ) )
							.count();
					assertThat( (long) cell.getNumericCellValue() ).isEqualTo(
							employeeDepartmentCounter );
					break;
				}
				count++;
			}
		}
	}


	@Test
	@DisplayName("Generate an Excel report containing all the employees")
	void givenNoInput_whenGenerateEmployeesExcelReport_thenReturnInputStreamResource()
			throws Exception {

		// given - precondition or setup
		departmentRepository.saveAll( departmentList );
		employeeRepository.saveAll( employeeList );

		// when - action or behaviour that we are going to test
		ResultActions response = mockMvc.perform( get( "/api/v1/reports/excel/employees" ) );

		// then - verify the output
		response.andExpect( status().isOk() );

		InputStream is = new ByteArrayInputStream(
				response.andReturn().getResponse().getContentAsByteArray() );


		//Create Workbook instance holding reference to .xlsx file
		XSSFWorkbook workbook = new XSSFWorkbook( is );

		//Get first/desired sheet from the workbook
		XSSFSheet sheet = workbook.getSheetAt( 0 );

		//Iterate through each row one by one
		Iterator<Row> rowIterator = sheet.iterator();

		// Skip rows that do not contain info given that I know the template of the xlsx
		skipRows( rowIterator, 3 );

		Row TotalRecordsRow = rowIterator.next();
		Iterator<Cell> TotalRecordsCellRowIterator = TotalRecordsRow.cellIterator();
		TotalRecordsCellRowIterator.next();
		assertThat((int)TotalRecordsCellRowIterator.next().getNumericCellValue() ).isEqualTo( employeeList.size() );


		skipRows( rowIterator, 1 );

		for ( int i = 0; i < employeeList.size(); i++ ) {
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();

			// FirstName | LastName  | Email | Salary |
			int count = 0;
			while ( cellIterator.hasNext() ) {
				Cell cell = cellIterator.next();
				//Check the cell type and format accordingly
				switch ( count ) {
				case 0:
					assertThat( cell.getStringCellValue() ).isEqualTo( employeeList.get( i ).getFirstName() );
					break;
				case 1:
					assertThat( cell.getStringCellValue() ).isEqualTo( employeeList.get( i ).getLastName() );
					break;
				case 2:
					assertThat( cell.getStringCellValue() ).isEqualTo( employeeList.get( i ).getEmail() );
					break;
				case 3:
					assertThat( BigDecimal.valueOf(
							Long.parseLong( formatter.format(cell.getNumericCellValue()  ) ) )).isEqualTo( employeeList.get( i ).getSalary() );
				}
				count++;
			}
		}


	}

	@Test
	@DisplayName("Generate a PDF report containing all the departments along with all the employees")
	void givenNoInput_whenGeneratePdfFullReport_thenReturnInputStreamResource() throws Exception {

		// given - precondition or setup
		departmentRepository.saveAll( departmentList );
		employeeRepository.saveAll( employeeList );

		// when - action or behaviour that we are going to test
		ResultActions response = mockMvc.perform( get( "/api/v1/reports/pdf/full-report" ) );

        // then - verify the output
        response
                // verify the status code that is returned
                .andExpect( status().isOk() );

	}

	@Test
	@DisplayName("Generate a zip file which contains two excel reports")
	void givenNoInput_whenGenerateAndZipReports_thenReturnInputStreamResource() throws Exception {

		// given - precondition or setup
		departmentRepository.saveAll( departmentList );
		employeeRepository.saveAll( employeeList );

		// when - action or behaviour that we are going to test
		ResultActions response = mockMvc.perform( get( "/api/v1/reports/zip" ) );

        // then - verify the output
        response
                // verify the status code that is returned
                .andExpect( status().isOk() );

	}

	@Test
	@DisplayName("Generate a multi-sheet Excel report containing departments and employees")
	void givenNoInput_whenGenerateMultiSheetExcelReport_thenReturnInputStreamResource()
			throws Exception {

		// given - precondition or setup
		departmentRepository.saveAll( departmentList );
		employeeRepository.saveAll( employeeList );

		// when - action or behaviour that we are going to test
		ResultActions response = mockMvc.perform( get( "/api/v1/reports/multi-sheet-excel" ) );

        // then - verify the output
        response
                // verify the status code that is returned
                .andExpect( status().isOk() );

	}

	private void skipRows( Iterator iterator, int num ) {
		for ( int i = 0; i < num; i++ ) {
			iterator.next();
		}
	}

}
