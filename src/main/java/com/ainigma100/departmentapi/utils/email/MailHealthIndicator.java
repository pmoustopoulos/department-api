package com.ainigma100.departmentapi.utils.email;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class MailHealthIndicator implements HealthIndicator {

    private final JavaMailSender mailSender;



    @Override
    public Health health() {
        try {
            testMailServerConnection();
            return Health.up().build();
        } catch (Exception e) {
            return Health.down().withDetail("error", e.getMessage()).build();
        }
    }

    private void testMailServerConnection() throws MessagingException {

        Session session = ((org.springframework.mail.javamail.JavaMailSenderImpl) mailSender).getSession();
        Transport transport = session.getTransport("smtp");

        try {
            transport.connect();
        } finally {
            transport.close(); // Ensure that the transport is closed even if an exception occurs.
        }
    }
}
