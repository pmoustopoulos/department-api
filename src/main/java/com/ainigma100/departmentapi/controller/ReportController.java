package com.ainigma100.departmentapi.controller;

import com.ainigma100.departmentapi.dto.FileDTO;
import com.ainigma100.departmentapi.enums.ReportLanguage;
import com.ainigma100.departmentapi.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor
@RequestMapping("/api/v1/reports")
@RestController
public class ReportController {


    private final ReportService reportService;

    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String ATTACHMENT_FILENAME = "attachment; filename=";



    @Operation(summary = "Generate an Excel report containing all the departments")
    @GetMapping("/excel/departments")
    public ResponseEntity<InputStreamResource> generateDepartmentsExcelReport() throws JRException {

        FileDTO report = reportService.generateDepartmentsExcelReport();

        InputStream targetStream = new ByteArrayInputStream(report.getFileContent());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(CONTENT_DISPOSITION, ATTACHMENT_FILENAME.concat(report.getFileName()));

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .contentLength(report.getFileContent().length)
                .body(new InputStreamResource(targetStream));

    }


    @Operation(summary = "Generate an Excel report containing all the employees")
    @GetMapping("/excel/employees")
    public ResponseEntity<InputStreamResource> generateEmployeesExcelReport() throws JRException {

        FileDTO report = reportService.generateEmployeesExcelReport();

        InputStream targetStream = new ByteArrayInputStream(report.getFileContent());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(CONTENT_DISPOSITION, ATTACHMENT_FILENAME.concat(report.getFileName()));

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .contentLength(report.getFileContent().length)
                .body(new InputStreamResource(targetStream));

    }


    @Operation(summary = "Generate a PDF report containing all the departments along with all the employees in the specified language")
    @GetMapping("/pdf/full-report")
    public ResponseEntity<InputStreamResource> generatePdfFullReport(
            @RequestParam ReportLanguage language) throws JRException {

        FileDTO report = reportService.generatePdfFullReport(language);

        InputStream targetStream = new ByteArrayInputStream(report.getFileContent());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(CONTENT_DISPOSITION, ATTACHMENT_FILENAME.concat(report.getFileName()));

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .contentLength(report.getFileContent().length)
                .body(new InputStreamResource(targetStream));

    }

    @Operation(summary = "Generate a combined PDF report from two separate reports in the specified language")
    @GetMapping("/pdf/combined-report")
    public ResponseEntity<InputStreamResource> generateCombinedPdfReport(
            @RequestParam ReportLanguage language) throws JRException {

        FileDTO report = reportService.generateCombinedPdfReport(language);

        InputStream targetStream = new ByteArrayInputStream(report.getFileContent());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(CONTENT_DISPOSITION, ATTACHMENT_FILENAME.concat(report.getFileName()));

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .contentLength(report.getFileContent().length)
                .body(new InputStreamResource(targetStream));
    }


    @Operation(summary = "Generate a zip file which contains two excel reports")
    @GetMapping("/zip")
    public ResponseEntity<InputStreamResource> generateAndZipReports() throws JRException, IOException {

        FileDTO report = reportService.generateAndZipReports();

        InputStream targetStream = new ByteArrayInputStream(report.getFileContent());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(CONTENT_DISPOSITION, ATTACHMENT_FILENAME.concat(report.getFileName()));

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .contentLength(report.getFileContent().length)
                .body(new InputStreamResource(targetStream));

    }


    @Operation(summary = "Generate a multi-sheet Excel report containing departments and employees")
    @GetMapping("/multi-sheet-excel")
    public ResponseEntity<InputStreamResource> generateMultiSheetExcelReport() throws JRException {

        FileDTO report = reportService.generateMultiSheetExcelReport();

        InputStream targetStream = new ByteArrayInputStream(report.getFileContent());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(CONTENT_DISPOSITION, ATTACHMENT_FILENAME.concat(report.getFileName()));

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .contentLength(report.getFileContent().length)
                .body(new InputStreamResource(targetStream));
    }


}
