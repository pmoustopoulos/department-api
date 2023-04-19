# Department API - Spring Boot 3

A RESTful API created using Spring Boot 3, Postgres, Docker, and JasperReport. The API allows for CRUD operations on departments and employees and includes functionality for generating Excel and PDF reports using JasperReport. Postgres database runs as a Docker container, which enables ease of setup and deployment. Additionally, Swagger is integrated for easy API documentation and testing.

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


* **Docker** - [Download & Install Docker Desktop](https://www.docker.com/products/docker-desktop/)


* **Jaspersoft Studio (Optional)** - [Download Jaspersoft Studio community edition](https://community.jaspersoft.com/project/jaspersoft-studio/releases)
Jaspersoft studio was used to create template files (.jrxml). These template files along with the jasper dependency was
used by Java to create excel snd pdf reports. This application is optional because you will need it only if you want to 
view or modify the template files.


## Start Postgres as a Docker container.
Start `Docker Desktop` and then execute the following command to start a docker container which will be running Postgres <br>

```shell
docker run -p 5432:5432 -d --name my-postgres-db -e POSTGRES_PASSWORD=pass -e POSTGRES_DB=mydb postgres
```

## Start the application
```shell
mvn spring-boot:run
```

## Swagger
Swagger was set on the root path, and you can access it on this URL: http://localhost:8080/

