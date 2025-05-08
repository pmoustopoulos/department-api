package com.ainigma100.departmentapi.service.impl;

import com.ainigma100.departmentapi.dto.DepartmentReportDTO;
import com.ainigma100.departmentapi.dto.EmployeeReportDTO;
import com.ainigma100.departmentapi.dto.FileDTO;
import com.ainigma100.departmentapi.entity.Department;
import com.ainigma100.departmentapi.entity.Employee;
import com.ainigma100.departmentapi.enums.ReportLanguage;
import com.ainigma100.departmentapi.mapper.DepartmentMapper;
import com.ainigma100.departmentapi.mapper.EmployeeMapper;
import com.ainigma100.departmentapi.repository.DepartmentRepository;
import com.ainigma100.departmentapi.repository.EmployeeRepository;
import com.ainigma100.departmentapi.utils.jasperreport.SimpleReportExporter;
import com.ainigma100.departmentapi.utils.jasperreport.SimpleReportFiller;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/*
 * @ExtendWith(MockitoExtension.class) informs Mockito that we are using
 * mockito annotations to mock the dependencies
 */
@ExtendWith(MockitoExtension.class)
@Tag("unit")
class ReportServiceImplTest {

	@Mock
	private DepartmentRepository departmentRepository;

	@Mock
	private EmployeeRepository employeeRepository;

	@Mock
	private DepartmentMapper departmentMapper;

	@Mock
	private EmployeeMapper employeeMapper;

	@Mock
	private SimpleReportExporter simpleReportExporter;

	@Mock
	private SimpleReportFiller simpleReportFiller;

	// Adding this mock to ensure that any dependency on ReloadableResourceBundleMessageSource
	// in the ReportServiceImpl class is properly handled. This mock will prevent any potential
	// null pointer exceptions or missing dependency issues during the tests.
	// To cover the messageSource instanceof ReloadableResourceBundleMessageSource bundleMessageSource
	@Mock
	private ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource;

//    @Mock
//    private MessageSource messageSource;



	// @InjectMocks creates the mock object of the class and injects the mocks
	// that are marked with the annotations @Mock into it.
	@InjectMocks
	private ReportServiceImpl reportService;

	private List<Department> departmentList;
	private List<DepartmentReportDTO> departmentReportDTOList;
	private List<Employee> employeeList;
	private List<EmployeeReportDTO> employeeReportDTOList;



	@BeforeEach
	void setUp() {

		Department department = new Department();
		department.setId(1L);
		department.setDepartmentCode("ABC");
		department.setDepartmentName("Department 1");
		department.setDepartmentDescription("Description 1");

		Department department2 = new Department();
		department2.setId(2L);
		department2.setDepartmentCode("DEF");
		department2.setDepartmentName("Department 2");
		department2.setDepartmentDescription("Description 2");

		DepartmentReportDTO departmentReportDTO = new DepartmentReportDTO();
		departmentReportDTO.setId(1L);
		departmentReportDTO.setDepartmentCode("ABC");
		departmentReportDTO.setDepartmentName("Department 1");
		departmentReportDTO.setDepartmentDescription("Description 1");

		DepartmentReportDTO departmentReportDTO2 = new DepartmentReportDTO();
		departmentReportDTO2.setId(2L);
		departmentReportDTO2.setDepartmentCode("DEF");
		departmentReportDTO2.setDepartmentName("Department 2");
		departmentReportDTO2.setDepartmentDescription("Description 2");

		Employee employee = new Employee();
		employee.setId("emp01");
		employee.setFirstName("John");
		employee.setLastName("Wick");
		employee.setEmail("jwick@gmail.com");
		employee.setSalary(BigDecimal.valueOf(40_000_000));
		employee.setDepartment(department);

		Employee employee2 = new Employee();
		employee2.setId("emp02");
		employee2.setFirstName("Marco");
		employee2.setLastName("Polo");
		employee2.setEmail("mpolo@gmail.com");
		employee2.setSalary(BigDecimal.valueOf(24_000_000));
		employee2.setDepartment(department2);

		EmployeeReportDTO employeeReportDTO = new EmployeeReportDTO();
		employeeReportDTO.setFirstName("John");
		employeeReportDTO.setLastName("Wick");
		employeeReportDTO.setEmail("jwick@gmail.com");
		employeeReportDTO.setSalary(BigDecimal.valueOf(40_000_000));

		EmployeeReportDTO employeeReportDTO2 = new EmployeeReportDTO();
		employeeReportDTO2.setFirstName("Marco");
		employeeReportDTO2.setLastName("Polo");
		employeeReportDTO2.setEmail("mpolo@gmail.com");
		employeeReportDTO2.setSalary(BigDecimal.valueOf(24_000_000));


		departmentList = Arrays.asList(department, department2);
		employeeList = Arrays.asList(employee, employee2);
		departmentReportDTOList = Arrays.asList(departmentReportDTO, departmentReportDTO2);
		employeeReportDTOList = Arrays.asList(employeeReportDTO, employeeReportDTO2);

	}

	@Test
	@DisplayName("Generate departments Excel report with data")
	void givenNoInput_whenGenerateDepartmentsExcelReport_thenReturnFileDTO() throws JRException {

		// given - precondition or setup
		given(departmentRepository.findAll()).willReturn(departmentList);
		given(departmentMapper.departmentToDepartmentReportDto(departmentList)).willReturn(departmentReportDTOList);
		given(simpleReportExporter.exportReportToByteArray(anyList(), anyString(), anyString())).willReturn(new byte[0]);


		// when - action or behaviour that we are going to test
		FileDTO fileDTO = reportService.generateDepartmentsExcelReport();

		// then - verify the output
		assertThat(fileDTO).isNotNull();
		assertThat(fileDTO.getFileName()).isNotNull();
		assertThat(fileDTO.getFileContent()).isNotNull();

		verify(departmentRepository, times(1)).findAll();
		verify(departmentMapper, times(1)).departmentToDepartmentReportDto(departmentList);
		verify(simpleReportExporter, times(1)).exportReportToByteArray(departmentReportDTOList, fileDTO.getFileName(), "jrxml/excel/departmentsExcelReport");
	}

	@Test
	@DisplayName("Generate departments Excel report with empty data")
	void givenNoInput_whenGenerateDepartmentsExcelReport_thenReturnFileDTOWithEmptyContent() throws JRException {

		// given - precondition or setup
		given(departmentRepository.findAll()).willReturn(Collections.emptyList());

		// when - action or behaviour that we are going to test
		FileDTO fileDTO = reportService.generateDepartmentsExcelReport();

		// then - verify the output
		assertThat(fileDTO).isNotNull();
		assertThat(fileDTO.getFileName()).isNotNull();
		assertThat(fileDTO.getFileContent()).isNull();

		verify(departmentRepository, times(1)).findAll();
		verify(departmentMapper, times(1)).departmentToDepartmentReportDto(Collections.emptyList());
		verify(simpleReportExporter, times(1)).exportReportToByteArray(Collections.emptyList(), fileDTO.getFileName(), "jrxml/excel/departmentsExcelReport");
	}

	@Test
	@DisplayName("Generate employees Excel report with data")
	void givenNoInput_whenGenerateEmployeesExcelReport_thenReturnFileDTO() throws JRException {

		// given - precondition or setup
		given(employeeRepository.findAll()).willReturn(employeeList);
		given(employeeMapper.employeeToEmployeeReportDto(employeeList)).willReturn(employeeReportDTOList);
		given(simpleReportExporter.exportReportToByteArray(anyList(), anyString(), anyString())).willReturn(new byte[0]);


		// when - action or behaviour that we are going to test
		FileDTO fileDTO = reportService.generateEmployeesExcelReport();

		// then - verify the output
		assertThat(fileDTO).isNotNull();
		assertThat(fileDTO.getFileName()).isNotNull();
		assertThat(fileDTO.getFileContent()).isNotNull();

		verify(employeeRepository, times(1)).findAll();
		verify(employeeMapper, times(1)).employeeToEmployeeReportDto(employeeList);
		verify(simpleReportExporter, times(1)).exportReportToByteArray(employeeReportDTOList, fileDTO.getFileName(), "jrxml/excel/employeesExcelReport");
	}

	@Test
	@DisplayName("Generate employees Excel report with empty data")
	void givenNoInput_whenGenerateEmployeesExcelReport_thenReturnFileDTOWithEmptyContent() throws JRException {

		// given - precondition or setup
		given(employeeRepository.findAll()).willReturn(Collections.emptyList());

		// when - action or behaviour that we are going to test
		FileDTO fileDTO = reportService.generateEmployeesExcelReport();

		// then - verify the output
		assertThat(fileDTO).isNotNull();
		assertThat(fileDTO.getFileName()).isNotNull();
		assertThat(fileDTO.getFileContent()).isNull();

		verify(employeeRepository, times(1)).findAll();
		verify(employeeMapper, times(1)).employeeToEmployeeReportDto(Collections.emptyList());
		verify(simpleReportExporter, times(1)).exportReportToByteArray(Collections.emptyList(), fileDTO.getFileName(), "jrxml/excel/employeesExcelReport");
	}

	@Test
	@DisplayName("Generate PDF full report (containing employees and departments) with data")
	void givenReportLanguage_whenGeneratePdfFullReport_thenReturnFileDTO() throws JRException {

		// given - precondition or setup
		ReportLanguage reportLanguage = ReportLanguage.EN;
		given(departmentRepository.findAll()).willReturn(departmentList);
		given(departmentMapper.departmentToDepartmentReportDto(departmentList)).willReturn(departmentReportDTOList);
		given(employeeRepository.findAll()).willReturn(employeeList);
		given(employeeMapper.employeeToEmployeeReportDto(employeeList)).willReturn(employeeReportDTOList);

		byte[] reportAsByteArray = "Mocked Report Data".getBytes();

		given(simpleReportFiller.compileReport("jrxml/pdf/subReport")).willReturn(mock(JasperReport.class));
		given(simpleReportExporter.getSubReportDataSource(employeeReportDTOList)).willReturn(mock(JRBeanCollectionDataSource.class));
		given(simpleReportExporter.exportReportToByteArray(eq(departmentReportDTOList), anyMap(), anyString(), eq("jrxml/pdf/mainReport")))
				.willReturn(reportAsByteArray);


		// when - action or behaviour that we are going to test
		FileDTO fileDTO = reportService.generatePdfFullReport(reportLanguage);

		// then - verify the output
		assertThat(fileDTO).isNotNull();
		assertThat(fileDTO.getFileName()).isNotNull();
		assertThat(fileDTO.getFileContent()).isNotNull();

		verify(departmentRepository, times(1)).findAll();
		verify(departmentMapper, times(1)).departmentToDepartmentReportDto(anyList());
		verify(employeeRepository, times(1)).findAll();
		verify(employeeMapper, times(1)).employeeToEmployeeReportDto(anyList());
		verify(simpleReportFiller, times(1)).compileReport(any(String.class));
		verify(simpleReportExporter, times(1)).getSubReportDataSource(anyList());
		verify(simpleReportExporter, times(1)).exportReportToByteArray(anyList(), anyMap(), anyString(), eq("jrxml/pdf/mainReport"));
	}

	@Test
	@DisplayName("Generate PDF full report with empty data")
	void givenReportLanguage_whenGeneratePdfFullReport_thenReturnFileDTOWithEmptyContent() throws JRException {

		// given - precondition or setup
		ReportLanguage reportLanguage = ReportLanguage.EN;
		given(departmentRepository.findAll()).willReturn(Collections.emptyList());
		given(departmentMapper.departmentToDepartmentReportDto(Collections.emptyList())).willReturn(Collections.emptyList());
		given(employeeRepository.findAll()).willReturn(Collections.emptyList());
		given(employeeMapper.employeeToEmployeeReportDto(Collections.emptyList())).willReturn(Collections.emptyList());

		given(simpleReportFiller.compileReport("jrxml/pdf/subReport")).willReturn(mock(JasperReport.class));
		given(simpleReportExporter.getSubReportDataSource(anyList())).willReturn(mock(JRBeanCollectionDataSource.class));
		given(simpleReportExporter.exportReportToByteArray(anyList(), anyMap(), anyString(), eq("jrxml/pdf/mainReport")))
				.willReturn(null);


		// when - action or behaviour that we are going to test
		FileDTO fileDTO = reportService.generatePdfFullReport(reportLanguage);

		// then - verify the output
		assertThat(fileDTO).isNotNull();
		assertThat(fileDTO.getFileName()).isNotNull();
		assertThat(fileDTO.getFileContent()).isNull();

		verify(departmentRepository, times(1)).findAll();
		verify(departmentMapper, times(1)).departmentToDepartmentReportDto(anyList());
		verify(employeeRepository, times(1)).findAll();
		verify(employeeMapper, times(1)).employeeToEmployeeReportDto(anyList());
		verify(simpleReportFiller, times(1)).compileReport(any(String.class));
		verify(simpleReportExporter, times(1)).getSubReportDataSource(anyList());
		verify(simpleReportExporter, times(1)).exportReportToByteArray(anyList(), anyMap(), anyString(), eq("jrxml/pdf/mainReport"));
	}

	@Test
	@DisplayName("Generate department and employee excel reports and then zip the reports")
	void givenNoInput_whenGenerateAndZipReports_thenReturnFileDTO() throws JRException, IOException {

		// given - precondition or setup
		given(employeeRepository.findAll()).willReturn(employeeList);
		given(employeeMapper.employeeToEmployeeReportDto(employeeList)).willReturn(employeeReportDTOList);
		JasperPrint mockEmployeeJasperPrint = mock(JasperPrint.class);
		given(simpleReportExporter.extractResultsToJasperPrint(eq(employeeReportDTOList), anyString(), anyString()))
				.willReturn(mockEmployeeJasperPrint);

		given(departmentRepository.findAll()).willReturn(departmentList);
		given(departmentMapper.departmentToDepartmentReportDto(departmentList)).willReturn(departmentReportDTOList);
		JasperPrint mockDepartmentJasperPrint = mock(JasperPrint.class);
		given(simpleReportExporter.extractResultsToJasperPrint(eq(departmentReportDTOList), anyString(), anyString()))
				.willReturn(mockDepartmentJasperPrint);

		List<JasperPrint> jasperPrintList = Arrays.asList(mockEmployeeJasperPrint, mockDepartmentJasperPrint);
		byte[] reportAsByteArray = "Mocked Zip Data".getBytes();

		given(simpleReportExporter.zipJasperPrintList(jasperPrintList)).willReturn(reportAsByteArray);


		// when - action or behaviour that we are going to test
		FileDTO fileDTO = reportService.generateAndZipReports();

		// then - verify the output
		assertThat(fileDTO).isNotNull();
		assertThat(fileDTO.getFileName()).isNotNull();
		assertThat(fileDTO.getFileContent()).isNotNull();

		// then - verify the output
		verify(employeeRepository, times(1)).findAll();
		verify(employeeMapper, times(1)).employeeToEmployeeReportDto(employeeList);
		verify(departmentRepository, times(1)).findAll();
		verify(departmentMapper, times(1)).departmentToDepartmentReportDto(departmentList);
		verify(simpleReportExporter, times(1)).zipJasperPrintList(jasperPrintList);
	}


	@Test
	@DisplayName("Generate empty department and empty employee excel reports and then zip the reports")
	void givenNoInput_whenGenerateAndZipReports_thenReturnFileDTOWithEmptyContent() throws JRException, IOException {

		// given - precondition or setup
		given(employeeRepository.findAll()).willReturn(Collections.emptyList());
		given(employeeMapper.employeeToEmployeeReportDto(Collections.emptyList())).willReturn(Collections.emptyList());
		JasperPrint mockEmployeeJasperPrint = mock(JasperPrint.class);
		given(simpleReportExporter.extractResultsToJasperPrint(eq(Collections.emptyList()), contains("Employee_Report_"), eq("jrxml/excel/employeesExcelReport")))
				.willReturn(mockEmployeeJasperPrint);

		given(departmentRepository.findAll()).willReturn(Collections.emptyList());
		given(departmentMapper.departmentToDepartmentReportDto(Collections.emptyList())).willReturn(Collections.emptyList());
		JasperPrint mockDepartmentJasperPrint = mock(JasperPrint.class);
		given(simpleReportExporter.extractResultsToJasperPrint(eq(Collections.emptyList()), contains("Department_Report_"), eq("jrxml/excel/departmentsExcelReport")))
				.willReturn(mockDepartmentJasperPrint);

		List<JasperPrint> jasperPrintList = Arrays.asList(mockEmployeeJasperPrint, mockDepartmentJasperPrint);
		byte[] reportAsByteArray = "Mocked Zip Data".getBytes();

		given(simpleReportExporter.zipJasperPrintList(eq(jasperPrintList))).willReturn(reportAsByteArray);

		// when - action or behavior that we are going to test
		FileDTO fileDTO = reportService.generateAndZipReports();

		// then - verify the output
		assertThat(fileDTO).isNotNull();
		assertThat(fileDTO.getFileName()).isNotNull();
		assertThat(fileDTO.getFileContent()).isNotNull();

		// then - verify the interactions
		verify(employeeRepository, times(1)).findAll();
		verify(employeeMapper, times(1)).employeeToEmployeeReportDto(Collections.emptyList());
		verify(simpleReportExporter, times(1)).extractResultsToJasperPrint(eq(Collections.emptyList()), contains("Employee_Report_"), eq("jrxml/excel/employeesExcelReport"));

		verify(departmentRepository, times(1)).findAll();
		verify(departmentMapper, times(1)).departmentToDepartmentReportDto(Collections.emptyList());
		verify(simpleReportExporter, times(1)).extractResultsToJasperPrint(eq(Collections.emptyList()), contains("Department_Report_"), eq("jrxml/excel/departmentsExcelReport"));

		verify(simpleReportExporter, times(1)).zipJasperPrintList(jasperPrintList);
	}

	@Test
	@DisplayName("Generate multi-sheet Excel report. One sheet contains departments and the other sheet contains employees")
	void givenNoInput_whenGenerateMultiSheetExcelReport_thenReturnFileDTO() throws JRException {

		// given - precondition or setup
		given(departmentRepository.findAll()).willReturn(departmentList);
		given(departmentMapper.departmentToDepartmentReportDto(departmentList)).willReturn(departmentReportDTOList);
		given(employeeRepository.findAll()).willReturn(employeeList);
		given(employeeMapper.employeeToEmployeeReportDto(employeeList)).willReturn(employeeReportDTOList);

		JasperReport departmentSubReport = mock(JasperReport.class);
		JRBeanCollectionDataSource departmentSubDataSource = mock(JRBeanCollectionDataSource.class);
		JasperReport employeeSubReport = mock(JasperReport.class);
		JRBeanCollectionDataSource employeeSubDataSource = mock(JRBeanCollectionDataSource.class);


		given(simpleReportFiller.compileReport("jrxml/excel/departmentsExcelReport")).willReturn(departmentSubReport);
		given(simpleReportFiller.compileReport("jrxml/excel/employeesExcelReport")).willReturn(employeeSubReport);
		given(simpleReportExporter.getSubReportDataSource(departmentReportDTOList)).willReturn(departmentSubDataSource);
		given(simpleReportExporter.getSubReportDataSource(employeeReportDTOList)).willReturn(employeeSubDataSource);

		Map<String, Object> jasperParameters = new HashMap<>();
		jasperParameters.put("departmentSubReport", departmentSubReport);
		jasperParameters.put("departmentSubDataSource", departmentSubDataSource);
		jasperParameters.put("employeeSubReport", employeeSubReport);
		jasperParameters.put("employeeSubDataSource", employeeSubDataSource);
		jasperParameters.put("firstSheetName", "DEPARTMENTS_REPORT");
		jasperParameters.put("secondSheetName", "EMPLOYEES_REPORT");

		given(simpleReportExporter.exportReportToByteArray(
				any(), eq(jasperParameters), anyString(), eq("jrxml/excel/multiSheetExcelReport")))
				.willReturn(new byte[]{}); // Provide empty byte array for simplicity


		// when - action or behavior that we are going to test
		FileDTO fileDTO = reportService.generateMultiSheetExcelReport();

		// then - verify the output
		assertThat(fileDTO).isNotNull();
		assertThat(fileDTO.getFileName()).startsWith("Multi_Sheet_Report_");
		assertThat(fileDTO.getFileContent()).isNotNull();

		verify(departmentRepository, times(1)).findAll();
		verify(departmentMapper, times(1)).departmentToDepartmentReportDto(departmentList);
		verify(employeeRepository, times(1)).findAll();
		verify(employeeMapper, times(1)).employeeToEmployeeReportDto(employeeList);
		verify(simpleReportFiller, times(1)).compileReport("jrxml/excel/departmentsExcelReport");
		verify(simpleReportFiller, times(1)).compileReport("jrxml/excel/employeesExcelReport");
		verify(simpleReportExporter, times(1)).getSubReportDataSource(departmentReportDTOList);
		verify(simpleReportExporter, times(1)).getSubReportDataSource(employeeReportDTOList);
		verify(simpleReportExporter, times(1)).exportReportToByteArray(any(), eq(jasperParameters), anyString(), eq("jrxml/excel/multiSheetExcelReport"));
	}



	@Test
	@DisplayName("Generate empty multi-sheet Excel report which only contains the sheets without records")
	void givenNoInput_whenGenerateMultiSheetExcelReport_thenReturnFileDTOWithEmptyContent() throws JRException {

		// given - precondition or setup
		given(departmentRepository.findAll()).willReturn(Collections.emptyList());
		given(departmentMapper.departmentToDepartmentReportDto(Collections.emptyList())).willReturn(Collections.emptyList());
		given(employeeRepository.findAll()).willReturn(Collections.emptyList());
		given(employeeMapper.employeeToEmployeeReportDto(Collections.emptyList())).willReturn(Collections.emptyList());

		JasperReport departmentSubReport = mock(JasperReport.class);
		JRBeanCollectionDataSource departmentSubDataSource = mock(JRBeanCollectionDataSource.class);
		JasperReport employeeSubReport = mock(JasperReport.class);
		JRBeanCollectionDataSource employeeSubDataSource = mock(JRBeanCollectionDataSource.class);

		given(simpleReportFiller.compileReport("jrxml/excel/departmentsExcelReport")).willReturn(departmentSubReport);
		given(simpleReportFiller.compileReport("jrxml/excel/employeesExcelReport")).willReturn(employeeSubReport);
		given(simpleReportExporter.getSubReportDataSource(Collections.emptyList())).willReturn(departmentSubDataSource);
		given(simpleReportExporter.getSubReportDataSource(Collections.emptyList())).willReturn(employeeSubDataSource);

		Map<String, Object> jasperParameters = new HashMap<>();
		jasperParameters.put("departmentSubReport", departmentSubReport);
		jasperParameters.put("departmentSubDataSource", departmentSubDataSource);
		jasperParameters.put("employeeSubReport", employeeSubReport);
		jasperParameters.put("employeeSubDataSource", employeeSubDataSource);
		jasperParameters.put("firstSheetName", "DEPARTMENTS_REPORT");
		jasperParameters.put("secondSheetName", "EMPLOYEES_REPORT");

		given(simpleReportExporter.exportReportToByteArray(
				any(), any(), contains("Multi_Sheet_Report_"), eq("jrxml/excel/multiSheetExcelReport")))
				.willReturn(new byte[]{}); // Provide empty byte array for simplicity

		// when - action or behavior that we are going to test
		FileDTO fileDTO = reportService.generateMultiSheetExcelReport();

		// then - verify the output
		assertThat(fileDTO).isNotNull();
		assertThat(fileDTO.getFileName()).startsWith("Multi_Sheet_Report_");
		assertThat(fileDTO.getFileContent()).isNotNull();

		verify(departmentRepository, times(1)).findAll();
		verify(departmentMapper, times(1)).departmentToDepartmentReportDto(Collections.emptyList());
		verify(employeeRepository, times(1)).findAll();
		verify(employeeMapper, times(1)).employeeToEmployeeReportDto(Collections.emptyList());
		verify(simpleReportFiller, times(1)).compileReport("jrxml/excel/departmentsExcelReport");
		verify(simpleReportFiller, times(1)).compileReport("jrxml/excel/employeesExcelReport");
		verify(simpleReportExporter, times(2)).getSubReportDataSource(Collections.emptyList());
		verify(simpleReportExporter, times(1)).exportReportToByteArray(any(), any(), contains("Multi_Sheet_Report_"), eq("jrxml/excel/multiSheetExcelReport"));
	}

	@Test
	@DisplayName("Generate combined PDF report with data")
	void givenReportLanguage_whenGenerateCombinedPdfReport_thenReturnFileDTO() throws JRException {

		// given - precondition or setup
		ReportLanguage reportLanguage = ReportLanguage.EN;
		JasperPrint mockJasperPrint = mock(JasperPrint.class);
		given(departmentRepository.findAll()).willReturn(departmentList);
		given(departmentMapper.departmentToDepartmentReportDto(departmentList)).willReturn(departmentReportDTOList);
		given(simpleReportExporter.extractResultsToJasperPrint(any(), any(), any(), any()))
				.willReturn(mockJasperPrint);



		given(employeeRepository.findAll()).willReturn(employeeList);
		given(employeeMapper.employeeToEmployeeReportDto(employeeList)).willReturn(employeeReportDTOList);
		given(simpleReportExporter.extractResultsToJasperPrint(any(), any(), any(), any()))
				.willReturn(mockJasperPrint);

        byte[] reportAsByteArray = "Mocked Report Data".getBytes();
        given(simpleReportExporter.combineAndExportPdf(anyList())).willReturn(reportAsByteArray);


		// when - action or behavior that we are going to test
		FileDTO fileDTO = reportService.generateCombinedPdfReport(reportLanguage);

		// then - verify the output
		assertThat(fileDTO).isNotNull();
		assertThat(fileDTO.getFileName()).isNotNull();
		assertThat(fileDTO.getFileContent()).isNotNull();
		assertThat(fileDTO.getFileName()).startsWith("EN_");;

        verify(departmentRepository, times(1)).findAll();
        verify(departmentMapper, times(1)).departmentToDepartmentReportDto(anyList());
        verify(simpleReportExporter, times(2)).extractResultsToJasperPrint(any(), any(), any(), any());
        verify(employeeRepository, times(1)).findAll();
        verify(employeeMapper, times(1)).employeeToEmployeeReportDto(anyList());
        verify(simpleReportExporter, times(1)).combineAndExportPdf(anyList());
    }


}
