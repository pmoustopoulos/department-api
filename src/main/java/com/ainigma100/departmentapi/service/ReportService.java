package com.ainigma100.departmentapi.service;

import com.ainigma100.departmentapi.dto.FileDTO;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;

public interface ReportService {

    FileDTO generateDepartmentsExcelReport() throws JRException;

    FileDTO generateEmployeesExcelReport() throws JRException;

    FileDTO generatePdfFullReport() throws JRException;

    FileDTO generateAndZipReports() throws JRException, IOException;

    FileDTO generateMultiSheetExcelReport() throws JRException;
}
