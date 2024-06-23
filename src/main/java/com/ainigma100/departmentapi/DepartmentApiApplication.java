package com.ainigma100.departmentapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class DepartmentApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DepartmentApiApplication.class, args);
    }


}
