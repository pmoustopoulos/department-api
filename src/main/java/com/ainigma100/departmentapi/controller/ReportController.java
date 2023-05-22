package com.ainigma100.departmentapi.controller;

import com.ainigma100.departmentapi.dto.FileDTO;
import com.ainigma100.departmentapi.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor
@RequestMapping("/api/v1/reports")
@RestController
public class ReportController {


    private final ReportService reportService;


    @Operation(summary = "Generate an Excel report containing all the departments")
    @GetMapping("/excel/departments")
    public ResponseEntity<InputStreamResource> generateDepartmentsExcelReport() throws JRException {

        FileDTO report = reportService.generateDepartmentsExcelReport();

        byte[] file = Base64.decodeBase64(report.getFileContent());
        InputStream targetStream = new ByteArrayInputStream(file);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "attachment; filename=".concat(report.getFileName()));

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .contentLength(file.length)
                .body(new InputStreamResource(targetStream));

    }


    @Operation(summary = "Generate an Excel report containing all the employees")
    @GetMapping("/excel/employees")
    public ResponseEntity<InputStreamResource> generateEmployeesExcelReport() throws JRException {

        FileDTO report = reportService.generateEmployeesExcelReport();

        byte[] file = Base64.decodeBase64(report.getFileContent());
        InputStream targetStream = new ByteArrayInputStream(file);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "attachment; filename=".concat(report.getFileName()));

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .contentLength(file.length)
                .body(new InputStreamResource(targetStream));

    }


    @Operation(summary = "Generate a PDF report containing all the departments along with all the employees")
    @GetMapping("/pdf/full-report")
    public ResponseEntity<InputStreamResource> generatePdfFullReport() throws JRException {

        FileDTO report = reportService.generatePdfFullReport();

        byte[] file = Base64.decodeBase64(report.getFileContent());
        InputStream targetStream = new ByteArrayInputStream(file);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "attachment; filename=".concat(report.getFileName()));

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .contentLength(file.length)
                .body(new InputStreamResource(targetStream));

    }

    @Operation(summary = "Generate a zip file which contains two excel reports")
    @GetMapping("/zip")
    public ResponseEntity<InputStreamResource> generateAndZipReports() throws JRException, IOException {

        FileDTO report = reportService.generateAndZipReports();

        byte[] file = Base64.decodeBase64(report.getFileContent());
        InputStream targetStream = new ByteArrayInputStream(file);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "attachment; filename=".concat(report.getFileName()));

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .contentLength(file.length)
                .body(new InputStreamResource(targetStream));

    }


    @Operation(summary = "Generate a multisheet Excel report containing departments and employees")
    @GetMapping("/multi-sheet-excel")
    public ResponseEntity<InputStreamResource> generateMultiSheetExcelReport() throws JRException {

        FileDTO report = reportService.generateMultiSheetExcelReport();

        byte[] file = Base64.decodeBase64(report.getFileContent());
        InputStream targetStream = new ByteArrayInputStream(file);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "attachment; filename=".concat(report.getFileName()));

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .contentLength(file.length)
                .body(new InputStreamResource(targetStream));
    }


}
