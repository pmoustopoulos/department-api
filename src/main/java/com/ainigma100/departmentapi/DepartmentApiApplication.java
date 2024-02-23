package com.ainigma100.departmentapi;

import com.ainigma100.departmentapi.filter.ServerDetails;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@OpenAPIDefinition(info =
@Info(title = "Department API", version = "${springdoc.version}", description = "Documentation ${spring.application.name} v1.0")
)
@SpringBootApplication
public class DepartmentApiApplication {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(DepartmentApiApplication.class);
        Environment environment = app.run(args).getEnvironment();
        ServerDetails serverDetails = new ServerDetails(environment);
        serverDetails.logServerDetails();

    }

}
