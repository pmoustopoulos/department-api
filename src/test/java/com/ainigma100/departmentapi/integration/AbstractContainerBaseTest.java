package com.ainigma100.departmentapi.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.function.Supplier;

public abstract class AbstractContainerBaseTest {

    static final PostgreSQLContainer POSTGRE_SQL_CONTAINER;
    static final Supplier<Object> DATABASE_DRIVER = () -> "org.postgresql.Driver";

    static final GenericContainer<?> MAILHOG_CONTAINER =
            new GenericContainer<>("mailhog/mailhog")
                    .withExposedPorts(1025, 8025);

    static {
        POSTGRE_SQL_CONTAINER = new PostgreSQLContainer("postgres:latest")
                .withDatabaseName("spring-boot-integration-test")
                .withUsername("postgres")
                .withPassword("pass");

        POSTGRE_SQL_CONTAINER.start();
        MAILHOG_CONTAINER.start();
    }

    // Dynamically fetch the values from the container and add it to the application context
    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry){

        // Postgres
        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", DATABASE_DRIVER);

        // MailHog
        Integer mailHogSMTPPort = MAILHOG_CONTAINER.getMappedPort(1025);
        registry.add("spring.mail.host", MAILHOG_CONTAINER::getHost);
        registry.add("spring.mail.port", mailHogSMTPPort::toString);
    }
}
