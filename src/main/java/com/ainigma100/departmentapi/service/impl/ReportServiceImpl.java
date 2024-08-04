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
import com.ainigma100.departmentapi.service.ReportService;
import com.ainigma100.departmentapi.utils.Utils;
import com.ainigma100.departmentapi.utils.annotation.ExecutionTime;
import com.ainigma100.departmentapi.utils.jasperreport.SimpleReportExporter;
import com.ainigma100.departmentapi.utils.jasperreport.SimpleReportFiller;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceResourceBundle;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;


@AllArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentMapper departmentMapper;
    private final EmployeeMapper employeeMapper;
    private final SimpleReportExporter simpleReportExporter;
    private final SimpleReportFiller simpleReportFiller;

    private final MessageSource messageSource;

    private static final String FILE_EXTENSION_XLSX = ".xlsx";
    private static final String EXCEL_DEPARTMENT_JRXML_PATH = "jrxml/excel/departmentsExcelReport";
    private static final String EXCEL_EMPLOYEE_JRXML_PATH = "jrxml/excel/employeesExcelReport";
    private static final String MULTI_SHEET_EXCEL_JRXML_PATH = "jrxml/excel/multiSheetExcelReport";
    public static final String JRXML_PDF_MAIN_REPORT = "jrxml/pdf/mainReport";
    private static final String JRXML_PDF_SUB_REPORT = "jrxml/pdf/subReport";



    @ExecutionTime
    @Override
    public FileDTO generateDepartmentsExcelReport() throws JRException {

        List<Department> departmentList = departmentRepository.findAll();
        List<DepartmentReportDTO> reportRecords = departmentMapper.departmentToDepartmentReportDto(departmentList);

        String dateAsString = Utils.getCurrentDateAsString();
        String fileName = "Department_Report_" + dateAsString + FILE_EXTENSION_XLSX;

        byte[] reportAsByteArray = simpleReportExporter.exportReportToByteArray(
                reportRecords, fileName, EXCEL_DEPARTMENT_JRXML_PATH);

        String base64String = Base64.encodeBase64String(reportAsByteArray);


        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(base64String);
        fileDTO.setFileName(fileName);

        return fileDTO;
    }


    @ExecutionTime
    @Override
    public FileDTO generateEmployeesExcelReport() throws JRException {

        List<Employee> employeeList = employeeRepository.findAll();
        List<EmployeeReportDTO> reportRecords = employeeMapper.employeeToEmployeeReportDto(employeeList);

        String dateAsString = Utils.getCurrentDateAsString();
        String fileName = "Employee_Report_" + dateAsString + FILE_EXTENSION_XLSX;

        byte[] reportAsByteArray = simpleReportExporter.exportReportToByteArray(
                reportRecords, fileName, EXCEL_EMPLOYEE_JRXML_PATH);

        String base64String = Base64.encodeBase64String(reportAsByteArray);


        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(base64String);
        fileDTO.setFileName(fileName);

        return fileDTO;
    }


    @ExecutionTime
    @Override
    public FileDTO generatePdfFullReport(ReportLanguage language) throws JRException {

        Locale locale = language != null ? language.getLocale() : Locale.ENGLISH;

        // Set the encoding based on the request language
        if (messageSource instanceof ReloadableResourceBundleMessageSource bundleMessageSource) {
            Utils.setEncodingForLocale(bundleMessageSource, locale);
        }
        MessageSourceResourceBundle resourceBundle = new MessageSourceResourceBundle(messageSource, locale);

        List<Department> departmentListMainReport = departmentRepository.findAll();
        List<DepartmentReportDTO> mainReportRecords = departmentMapper.departmentToDepartmentReportDto(departmentListMainReport);

        List<Employee> employeeListSubReport = employeeRepository.findAll();
        List<EmployeeReportDTO> subReportRecords = employeeMapper.employeeToEmployeeReportDto(employeeListSubReport);

        String dateAsString = Utils.getCurrentDateAsString();
        String fileName = language + "_Full_Report_" + dateAsString + ".pdf";

        // prepare the sub report
        JasperReport subReport = simpleReportFiller.compileReport(JRXML_PDF_SUB_REPORT);
        JRBeanCollectionDataSource subDataSource = simpleReportExporter.getSubReportDataSource(subReportRecords);

        // add the sub report as parameter to the main report
        Map<String, Object> jasperParameters = new HashMap<>();
        jasperParameters.put("subReport", subReport);
        jasperParameters.put("subDataSource", subDataSource);
        jasperParameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);
        jasperParameters.put(JRParameter.REPORT_LOCALE, locale);

        byte[] reportAsByteArray = simpleReportExporter.exportReportToByteArray(
                mainReportRecords,
                jasperParameters,
                fileName,
                JRXML_PDF_MAIN_REPORT);

        String base64String = Base64.encodeBase64String(reportAsByteArray);


        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(base64String);
        fileDTO.setFileName(fileName);

        return fileDTO;
    }

    @Override
    public FileDTO generateAndZipReports() throws JRException, IOException {

        String dateAsString = Utils.getCurrentDateAsString();


        List<Employee> employeeList = employeeRepository.findAll();
        List<EmployeeReportDTO> employeeReportRecords = employeeMapper.employeeToEmployeeReportDto(employeeList);
        String employeeFileName = "Employee_Report_" + dateAsString + FILE_EXTENSION_XLSX;

        JasperPrint jasperPrintEmployees = simpleReportExporter.extractResultsToJasperPrint(employeeReportRecords, employeeFileName, EXCEL_EMPLOYEE_JRXML_PATH);


        List<Department> departmentList = departmentRepository.findAll();
        List<DepartmentReportDTO> departmentReportRecords = departmentMapper.departmentToDepartmentReportDto(departmentList);
        String departmentFileName = "Department_Report_" + dateAsString + FILE_EXTENSION_XLSX;

        JasperPrint jasperPrintDepartments = simpleReportExporter.extractResultsToJasperPrint(departmentReportRecords, departmentFileName, EXCEL_DEPARTMENT_JRXML_PATH);


        List<JasperPrint> listOfJasperPrints = new ArrayList<>();

        listOfJasperPrints.add(jasperPrintEmployees);
        listOfJasperPrints.add(jasperPrintDepartments);

        byte[] reportAsByteArray = simpleReportExporter.zipJasperPrintList(listOfJasperPrints);


        String base64String = Base64.encodeBase64String(reportAsByteArray);

        String fileName = "Multiple_Reports_" + dateAsString + ".zip";

        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(base64String);
        fileDTO.setFileName(fileName);

        return fileDTO;
    }

    @Override
    public FileDTO generateMultiSheetExcelReport() throws JRException {

        String dateAsString = Utils.getCurrentDateAsString();
        String excelFileName = "Multi_Sheet_Report_" + dateAsString + FILE_EXTENSION_XLSX;

        List<Department> departmentList = departmentRepository.findAll();
        List<DepartmentReportDTO> mappedDepartmentRecords = departmentMapper.departmentToDepartmentReportDto(departmentList);

        List<Employee> employeeList = employeeRepository.findAll();
        List<EmployeeReportDTO> mappedEmployeeRecords = employeeMapper.employeeToEmployeeReportDto(employeeList);

        // prepare department sub report
        JasperReport departmentSubReport = simpleReportFiller.compileReport(EXCEL_DEPARTMENT_JRXML_PATH);
        JRBeanCollectionDataSource departmentSubDataSource = simpleReportExporter.getSubReportDataSource(mappedDepartmentRecords);


        // prepare employee sub report
        JasperReport employeeSubReport = simpleReportFiller.compileReport(EXCEL_EMPLOYEE_JRXML_PATH);
        JRBeanCollectionDataSource employeeSubDataSource = simpleReportExporter.getSubReportDataSource(mappedEmployeeRecords);


        // add sub reports as parameters to Jasper Report
        Map<String, Object> jasperParameters = new HashMap<>();
        jasperParameters.put("departmentSubReport", departmentSubReport);
        jasperParameters.put("departmentSubDataSource", departmentSubDataSource);
        jasperParameters.put("employeeSubReport", employeeSubReport);
        jasperParameters.put("employeeSubDataSource", employeeSubDataSource);

        // set sheet names for each sub report
        jasperParameters.put("firstSheetName", "DEPARTMENTS_REPORT");
        jasperParameters.put("secondSheetName", "EMPLOYEES_REPORT");


        byte[] reportAsByteArray = simpleReportExporter.exportReportToByteArray(
                null, jasperParameters, excelFileName, MULTI_SHEET_EXCEL_JRXML_PATH);

        String base64String = Base64.encodeBase64String(reportAsByteArray);


        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(base64String);
        fileDTO.setFileName(excelFileName);

        return fileDTO;
    }

    @Override
    public FileDTO generateCombinedPdfReport(ReportLanguage language) throws JRException {

        MessageSourceResourceBundle resourceBundle = setupLocaleAndMessages(language);


        // add the sub report as parameter to the main report
        Map<String, Object> jasperParameters = new HashMap<>();
        jasperParameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);
        jasperParameters.put(JRParameter.REPORT_LOCALE, resourceBundle.getLocale());

        // Generate the first report part
        List<Department> departmentList = departmentRepository.findAll();
        List<DepartmentReportDTO> mappedDepartmentRecords = departmentMapper.departmentToDepartmentReportDto(departmentList);
        JasperPrint jasperPrintPage1 = simpleReportExporter.extractResultsToJasperPrint(mappedDepartmentRecords, jasperParameters, "page1.pdf", JRXML_PDF_MAIN_REPORT);

        // Generate the second report part
        List<Employee> employeeList = employeeRepository.findAll();
        List<EmployeeReportDTO> mappedEmployeeRecords = employeeMapper.employeeToEmployeeReportDto(employeeList);
        JasperPrint jasperPrintPage2 = simpleReportExporter.extractResultsToJasperPrint(mappedEmployeeRecords, jasperParameters, "page2.pdf", JRXML_PDF_SUB_REPORT);


        // Combine the JasperPrint objects
        List<JasperPrint> jasperPrintList = Arrays.asList(jasperPrintPage1, jasperPrintPage2);
        byte[] combinedPdf = simpleReportExporter.combineAndExportPdf(jasperPrintList);

        String dateAsString = Utils.getCurrentDateAsString();
        String fileName = language + "_Full_Combined_Report_" + dateAsString + ".pdf";

        String base64String = Base64.encodeBase64String(combinedPdf);

        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(base64String);
        fileDTO.setFileName(fileName);

        return fileDTO;
    }


    private MessageSourceResourceBundle setupLocaleAndMessages(ReportLanguage language) {

        // Set the encoding based on the request language
        Locale locale = Utils.retrieveValueOrSetDefault(() -> language.getLocale(), Locale.ENGLISH);

        // Set the encoding based on the request language
        Utils.setEncodingForLocale((ReloadableResourceBundleMessageSource) messageSource, locale);

        return new MessageSourceResourceBundle(messageSource, locale);
    }

}
