package com.ainigma100.departmentapi.controller;

import com.ainigma100.departmentapi.dto.FileDTO;
import com.ainigma100.departmentapi.enums.ReportLanguage;
import com.ainigma100.departmentapi.service.ReportService;
import com.ainigma100.departmentapi.utils.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * @WebMvcTest annotation will load all the components required
 * to test the Controller layer. It will not load the service or repository layer components
 */
@WebMvcTest(ReportController.class)
@Tag("unit")
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;


    private FileDTO report;


    @BeforeEach
    void setUp() throws IOException {

        String filePath = "jsonfile/mockedFileDTO.json";

        String fileName = TestHelper.extractJsonPropertyFromFile(filePath, "fileName");
        String fileContent = TestHelper.extractJsonPropertyFromFile(filePath, "fileContent");

        report = new FileDTO(fileName, fileContent);

    }


    @Test
    @DisplayName("Generate an Excel report containing all the departments")
    void givenNoInput_whenGenerateDepartmentsExcelReport_thenReturnInputStreamResource() throws Exception {

        // given - precondition or setup
        String filePath = "jsonfile/mockedFileDTO.json";


        String fileName = TestHelper.extractJsonPropertyFromFile(filePath, "fileName");
        String fileContent = TestHelper.extractJsonPropertyFromFile(filePath, "fileContent");

        FileDTO report = new FileDTO(fileName, fileContent);

        given(reportService.generateDepartmentsExcelReport()).willReturn(report);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/reports/excel/departments"));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Generate an Excel report containing all the employees")
    void givenNoInput_whenGenerateEmployeesExcelReport_thenReturnInputStreamResource() throws Exception {

        // given - precondition or setup
        given(reportService.generateEmployeesExcelReport()).willReturn(report);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/reports/excel/employees"));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Generate a PDF report containing all the departments along with all the employees in the specified language")
    void givenReportLanguage_whenGeneratePdfFullReport_thenReturnInputStreamResource() throws Exception {

        // given - precondition or setup
        ReportLanguage reportLanguage = ReportLanguage.EN;
        given(reportService.generatePdfFullReport(reportLanguage)).willReturn(report);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/reports/pdf/full-report")
                .contentType(MediaType.APPLICATION_JSON)
                .param("language", String.valueOf(reportLanguage)));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Generate a combined PDF report from two separate reports in the specified language")
    void givenReportLanguage_whenGenerateCombinedPdfReport_thenReturnInputStreamResource() throws Exception {

        // given - precondition or setup
        ReportLanguage reportLanguage = ReportLanguage.EN;
        given(reportService.generateCombinedPdfReport(reportLanguage)).willReturn(report);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/reports/pdf/combined-report")
                .contentType(MediaType.APPLICATION_JSON)
                .param("language", String.valueOf(reportLanguage)));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk());

    }



    @Test
    @DisplayName("Generate a zip file which contains two excel reports")
    void givenNoInput_whenGenerateAndZipReports_thenReturnInputStreamResource() throws Exception {

        // given - precondition or setup
        given(reportService.generateAndZipReports()).willReturn(report);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/reports/zip"));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Generate a multi-sheet Excel report containing departments and employees")
    void givenNoInput_whenGenerateMultiSheetExcelReport_thenReturnInputStreamResource() throws Exception {

        // given - precondition or setup
        given(reportService.generateMultiSheetExcelReport()).willReturn(report);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/reports/multi-sheet-excel"));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk());

    }


}
