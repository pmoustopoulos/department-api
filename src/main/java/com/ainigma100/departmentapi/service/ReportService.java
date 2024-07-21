package com.ainigma100.departmentapi.service;

import com.ainigma100.departmentapi.dto.FileDTO;
import com.ainigma100.departmentapi.enums.ReportLanguage;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;

public interface ReportService {

    FileDTO generateDepartmentsExcelReport() throws JRException;

    FileDTO generateEmployeesExcelReport() throws JRException;

    FileDTO generatePdfFullReport(ReportLanguage language) throws JRException;

    FileDTO generateAndZipReports() throws JRException, IOException;

    FileDTO generateMultiSheetExcelReport() throws JRException;

    FileDTO generateCombinedPdfReport(ReportLanguage language) throws JRException;
}
