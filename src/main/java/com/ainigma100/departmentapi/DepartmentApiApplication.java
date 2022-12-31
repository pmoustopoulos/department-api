package com.ainigma100.departmentapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info =
@Info(title = "Department API", version = "${springdoc.version}", description = "Documentation ${spring.application.name} v1.0")
)
@SpringBootApplication
public class DepartmentApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DepartmentApiApplication.class, args);
    }

}
