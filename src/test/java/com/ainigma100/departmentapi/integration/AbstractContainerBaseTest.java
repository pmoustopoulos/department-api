package com.ainigma100.departmentapi.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.function.Supplier;

public abstract class AbstractContainerBaseTest {

    static final PostgreSQLContainer POSTGRE_SQL_CONTAINER;
    static final Supplier<Object> DATABASE_DRIVER = () -> "org.postgresql.Driver";


    static {
        POSTGRE_SQL_CONTAINER = new PostgreSQLContainer("postgres:latest")
                .withDatabaseName("spring-boot-integration-test")
                .withUsername("postgres")
                .withPassword("pass");

        POSTGRE_SQL_CONTAINER.start();

    }

    // Dynamically fetch the values from the container and add it to the application context
    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", DATABASE_DRIVER);
    }
}
