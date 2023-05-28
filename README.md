# Department API - Spring Boot 3

A RESTful API created using Spring Boot 3, H2 Database, and JasperReport. The API allows for CRUD operations on departments and employees and includes functionality for generating Excel and PDF reports using JasperReport. Additionally, Swagger is integrated for easy API documentation and testing.

## Run with an In-Memory H2 Database
If you want to run the application with an **in-memory H2 database**, use the `h2-database` branch.
This branch includes the necessary configuration files and dependencies to set up and use H2 as the database for the application.
To get started, simply switch to the `h2-database branch` and run the application, but all data and related information
will be persisted to a file on the local file system.


## Prerequisites
Make sure you have installed all the following prerequisites on your development machine:

* **Java 17** - You will need at least Java 17 installed on your machine because it is required by **Spring Boot 3**.
  If you are using IntelliJ, you can easily download it directly from the IDE.
  `File -> Project Structure -> Project -> SDK -> Add SDK -> Download JDK...`. Alternatively, you can download it from
  here: [Download & Install Java 17](https://www.oracle.com/java/technologies/downloads/#java17)



* **Jaspersoft Studio (Optional)** - [Download Jaspersoft Studio community edition](https://community.jaspersoft.com/project/jaspersoft-studio/releases)
  Jaspersoft studio was used to create template files (.jrxml). These template files along with the jasper dependency was
  used by Java to create excel and pdf reports. This application is optional because you will need it only if you want to
  view or modify the template files.

  

## Start the application
```shell
./mvnw spring-boot:run
```

## Swagger
Swagger was set on the root path, and you can access it on this URL: http://localhost:8080/

The API also allows for `generating various reports` using `JasperReport`, such as generating an Excel file, generating a PDF file, generating a zipped folder that contains reports, and generating a single Excel file that contains multiple sheets inside.


## Testing

This application includes unit testing and integration testing using JUnit 5, Mockito, and Spring's `WebMvcTest`. The tests are written in a BDD (`Behavior-Driven Development`) style.

### Unit Testing

Unit tests are written using JUnit 5 and Mockito in a `BDD style`, focusing on describing the behavior of individual units of code.


```shell
./mvnw test
```