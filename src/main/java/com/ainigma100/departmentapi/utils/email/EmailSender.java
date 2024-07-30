package com.ainigma100.departmentapi.utils.email;

import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;


@Slf4j
@AllArgsConstructor
@Component
public class EmailSender {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;


    @Async
    public void sendEmail(EmailRequest emailRequest) {

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            // Set email properties
            helper.setFrom(emailRequest.getFrom());
            helper.setSubject(emailRequest.getSubject());
            helper.setTo(emailRequest.getToRecipients().toArray(new String[0]));

            final Context context = new Context();
            // set variables contained inside the email body (thymeleaf html file)
            context.setVariables(emailRequest.getDynamicVariables());

            // Set email body
            String processedEmailBody = templateEngine.process(emailRequest.getEmailBody(), context);
            helper.setText(processedEmailBody, true);

            // Attach dynamic images inline
            this.attachImagesInline(emailRequest, helper);
            // Attach dynamic attachments
            this.addAttachments(helper, emailRequest.getAttachments());

            if (emailRequest.getCcRecipients() != null && !emailRequest.getCcRecipients().isEmpty()) {
                helper.setCc(emailRequest.getCcRecipients().toArray(new String[0]));
            }

            // Send email
            javaMailSender.send(mimeMessage);

            log.info("Email sent successfully");

        } catch (MessagingException e) {
            log.error("Error sending email", e);
        }
    }


    private void attachImagesInline(EmailRequest emailRequest, MimeMessageHelper helper) throws MessagingException {

        if (emailRequest.getImagePaths() != null && !emailRequest.getImagePaths().isEmpty()) {

            for (Map.Entry<String, String> entry : emailRequest.getImagePaths().entrySet()) {

                String variableName = entry.getKey();
                String imagePath = entry.getValue();
                ClassPathResource imageResource = new ClassPathResource(imagePath);

                if (imageResource.exists()) {
                    helper.addInline(variableName, imageResource);
                }
            }
        }
    }


    private void addAttachments(MimeMessageHelper helper, Map<String, DataSource> attachments) throws MessagingException {

        if (attachments != null && !attachments.isEmpty()) {

            for (Map.Entry<String, DataSource> attachmentEntry : attachments.entrySet()) {

                String attachmentFilename = attachmentEntry.getKey();
                DataSource attachmentDataSource = attachmentEntry.getValue();

                helper.addAttachment(attachmentFilename, attachmentDataSource);
            }
        }
    }

}
