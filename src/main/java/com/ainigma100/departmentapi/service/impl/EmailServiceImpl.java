package com.ainigma100.departmentapi.service.impl;

import com.ainigma100.departmentapi.dto.FileDTO;
import com.ainigma100.departmentapi.enums.ReportLanguage;
import com.ainigma100.departmentapi.service.EmailService;
import com.ainigma100.departmentapi.service.ReportService;
import com.ainigma100.departmentapi.utils.email.EmailRequest;
import com.ainigma100.departmentapi.utils.email.EmailSender;
import jakarta.activation.DataSource;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private final EmailSender emailSender;
    private final ReportService reportService;


    @Override
    public Boolean sendEmailWithoutAttachment() {

        List<String> toRecipients = List.of("jwick@gmail.com");
        List<String> ccRecipients = List.of("mpolo@gmail.com");

        Map<String, Object> dynamicVariables = new HashMap<>();
        dynamicVariables.put("recipientName", "John Wick");
        dynamicVariables.put("githubRepoUrl", "https://github.com/pmoustopoulos/department-api");

        Map<String, String> imagePaths = new HashMap<>();
        imagePaths.put("logo", "reportLogo/luffy.png");

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setFrom("lyffy@pirateking.com");
        emailRequest.setEmailBody("email-template.html");
        emailRequest.setAttachments(null);
        emailRequest.setSubject("Test Email");
        emailRequest.setToRecipients(toRecipients);
        emailRequest.setCcRecipients(ccRecipients);
        emailRequest.setDynamicVariables(dynamicVariables);
        emailRequest.setImagePaths(imagePaths);


        emailSender.sendEmail(emailRequest);

        return true;
    }


    @Override
    public Boolean sendEmailWithAttachment() throws JRException {

        List<String> toRecipients = List.of("jwick@gmail.com", "maria@gmail.com");
        List<String> ccRecipients = List.of("mpolo@gmail.com", "nick@gmail.com");

        Map<String, Object> dynamicVariables = new HashMap<>();
        dynamicVariables.put("recipientName", "John Wick");
        dynamicVariables.put("githubRepoUrl", "https://github.com/pmoustopoulos/department-api");

        Map<String, String> imagePaths = new HashMap<>();
        imagePaths.put("logo", "reportLogo/luffy.png");

        // generate the first attachment
        FileDTO excelAttachment = reportService.generateDepartmentsExcelReport();
        DataSource excelDataSource = new ByteArrayDataSource(excelAttachment.getFileContent(), "application/octet-stream");

        // generate the second attachment
        FileDTO pdfAttachment = reportService.generatePdfFullReport(ReportLanguage.EN);
        DataSource pdfDataSource = new ByteArrayDataSource(pdfAttachment.getFileContent(), "application/octet-stream");


        // Create a map of attachments
        Map<String, DataSource> attachments = new HashMap<>();
        attachments.put(excelAttachment.getFileName(), excelDataSource);
        attachments.put(pdfAttachment.getFileName(), pdfDataSource);


        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setFrom("lyffy@pirateking.com");
        emailRequest.setEmailBody("email-template.html");
        emailRequest.setAttachments(attachments);
        emailRequest.setSubject("Test Email");
        emailRequest.setToRecipients(toRecipients);
        emailRequest.setCcRecipients(ccRecipients);
        emailRequest.setDynamicVariables(dynamicVariables);
        emailRequest.setImagePaths(imagePaths);


        emailSender.sendEmail(emailRequest);

        return true;
    }



}
