package com.ainigma100.departmentapi.service.impl;

import com.ainigma100.departmentapi.dto.DepartmentReportDTO;
import com.ainigma100.departmentapi.dto.EmployeeReportDTO;
import com.ainigma100.departmentapi.dto.FileDTO;
import com.ainigma100.departmentapi.entity.Department;
import com.ainigma100.departmentapi.entity.Employee;
import com.ainigma100.departmentapi.mapper.DepartmentMapper;
import com.ainigma100.departmentapi.mapper.EmployeeMapper;
import com.ainigma100.departmentapi.repository.DepartmentRepository;
import com.ainigma100.departmentapi.repository.EmployeeRepository;
import com.ainigma100.departmentapi.service.ReportService;
import com.ainigma100.departmentapi.utils.annotation.ExecutionTime;
import com.ainigma100.departmentapi.utils.jasperreport.SimpleReportExporter;
import com.ainigma100.departmentapi.utils.jasperreport.SimpleReportFiller;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@AllArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {

    private DepartmentRepository departmentRepository;
    private EmployeeRepository employeeRepository;
    private DepartmentMapper departmentMapper;
    private EmployeeMapper employeeMapper;
    private SimpleReportExporter reportExporter;
    private SimpleReportFiller simpleReportFiller;


    @ExecutionTime
    @Override
    public FileDTO generateDepartmentsExcelReport() throws JRException {

        List<Department> departmentList = departmentRepository.findAll();
        List<DepartmentReportDTO> reportRecords = departmentMapper.departmentToDepartmentReportDto(departmentList);

        String dateAsString = this.getCurrentDateAsString();
        String fileName = "Department_Report_" + dateAsString + ".xlsx";

        byte[] reportAsByteArray = reportExporter.exportReportToByteArray(
                reportRecords, fileName, "jrxml/excel/departmentsExcelReport");

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

        String dateAsString = this.getCurrentDateAsString();
        String fileName = "Employee_Report_" + dateAsString + ".xlsx";

        byte[] reportAsByteArray = reportExporter.exportReportToByteArray(
                reportRecords, fileName, "jrxml/excel/employeesExcelReport");

        String base64String = Base64.encodeBase64String(reportAsByteArray);


        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(base64String);
        fileDTO.setFileName(fileName);

        return fileDTO;
    }


    @ExecutionTime
    @Override
    public FileDTO generatePdfFullReport() throws JRException {

        List<Department> departmentListMainReport = departmentRepository.findAll();
        List<DepartmentReportDTO> mainReportRecords = departmentMapper.departmentToDepartmentReportDto(departmentListMainReport);

        List<Employee> employeeListSubReport = employeeRepository.findAll();
        List<EmployeeReportDTO> subReportRecords = employeeMapper.employeeToEmployeeReportDto(employeeListSubReport);

        String dateAsString = this.getCurrentDateAsString();
        String fileName = "Full_Report_" + dateAsString + ".pdf";

        // prepare the sub report
        JasperReport subReport = simpleReportFiller.compileReport("jrxml/pdf/subReport");
        JRBeanCollectionDataSource subDataSource = reportExporter.getSubReportDataSource(subReportRecords);

        // add the sub report as parameter to the main report
        Map<String, Object> jasperParameters = new HashMap<>();
        jasperParameters.put("subReport", subReport);
        jasperParameters.put("subDataSource", subDataSource);

        byte[] reportAsByteArray = reportExporter.exportReportToByteArray(
                mainReportRecords,
                jasperParameters,
                fileName,
                "jrxml/pdf/mainReport");

        String base64String = Base64.encodeBase64String(reportAsByteArray);


        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(base64String);
        fileDTO.setFileName(fileName);

        return fileDTO;
    }

    @Override
    public FileDTO generateAndZipReports() throws JRException, IOException {

        String dateAsString = this.getCurrentDateAsString();


        List<Employee> employeeList = employeeRepository.findAll();
        List<EmployeeReportDTO> employeeReportRecords = employeeMapper.employeeToEmployeeReportDto(employeeList);
        String employeeFileName = "Employee_Report_" + dateAsString + ".xlsx";

        JasperPrint jasperPrintEmployees = reportExporter.extractResultsToJasperPrint(employeeReportRecords, employeeFileName, "jrxml/excel/employeesExcelReport");


        List<Department> departmentList = departmentRepository.findAll();
        List<DepartmentReportDTO> departmentReportRecords = departmentMapper.departmentToDepartmentReportDto(departmentList);
        String departmentFileName = "Department_Report_" + dateAsString + ".xlsx";

        JasperPrint jasperPrintDepartments = reportExporter.extractResultsToJasperPrint(departmentReportRecords, departmentFileName, "jrxml/excel/departmentsExcelReport");


        List<JasperPrint> listOfJasperPrints = new ArrayList<>();

        listOfJasperPrints.add(jasperPrintEmployees);
        listOfJasperPrints.add(jasperPrintDepartments);

        byte[] reportAsByteArray = reportExporter.zipJasperPrintList(listOfJasperPrints);


        String base64String = Base64.encodeBase64String(reportAsByteArray);

        String fileName = "Multiple_Reports_" + dateAsString + ".zip";

        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(base64String);
        fileDTO.setFileName(fileName);

        return fileDTO;
    }


    private String getCurrentDateAsString() {

        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return dateTimeFormatter.format(localDate);
    }

}
