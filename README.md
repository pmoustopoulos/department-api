# Department API - Spring Boot 3

A RESTful API created using Spring Boot 3, Postgres, Docker, and JasperReport. The API allows for CRUD operations 
on departments and employees and includes functionality for generating Excel and PDF reports using JasperReport. 
Postgres database runs as a Docker container, which enables ease of setup and deployment. Additionally, 
Swagger is integrated for easy API documentation and testing.

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
`Integration tests use Testcontainers`, which requires Docker Desktop to be installed and running
  on your local machine. Docker Desktop provides the necessary environment to spin up containers for the tests. <br>
  **NOTE**: Make sure Docker Desktop is installed and running before running the integration tests.


* **Jaspersoft Studio (Optional)** - [Download Jaspersoft Studio community edition](https://community.jaspersoft.com/project/jaspersoft-studio/releases)
  Jaspersoft studio was used to create template files (.jrxml). These template files along with the jasper dependency was
  used by Java to create excel and pdf reports. This application is optional because you will need it only if you want to
  view or modify the template files.

## Start App as a Docker container.
1. First, build the application using the Maven wrapper by running the following command in the terminal:
 ```shell
./mvnw install -DskipTests
```
This command will build the application and generate a jar file located at target/department-api-0.0.1-SNAPSHOT.jar.

2. Make sure you have `Docker Desktop` installed and running on your machine.

3. Start the Docker container by executing the following command:
```shell
docker-compose up --build   
```

iThis command will build the Docker image and start the container. If you want to detach from the terminal and run the
container in the background, you can add the `-d` flag
```shell
docker-compose up --build -d   
```
The application should now be running normally within the Docker container, accessible via the predefined ports.

4. To stop the containers and shut down the application, use the following command:
```shell
 docker-compose stop
```

5. To start the application without rebuilding the Docker images use the following command:
```shell
 docker-compose stop
```

If you want to remove the containers completely, including any associated networks and volumes,
you can run the following command:
```shell
 docker-compose down
```
This will stop and remove the containers, networks, and volumes created by Docker-compose.

**Make sure you have Docker and Docker Compose properly installed and configured before following these steps.**


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


The API also allows for `generating various reports` using `JasperReport`, such as generating an Excel file, generating 
a PDF file, generating a zipped folder that contains reports, and generating a single Excel file that contains multiple 
sheets inside.

## Testing

This application includes unit testing and integration testing using JUnit 5, Mockito, and Spring's `WebMvcTest`.
The tests are written in a BDD (`Behavior-Driven Development`) style.


### Unit Testing

Unit tests are written using JUnit 5 and Mockito in a `BDD style`, focusing on describing the behavior of 
individual units of code.


### Integration Testing with Testcontainers
`Integration tests` are performed `using Testcontainers`, a powerful Java library that provides lightweight, disposable
containers for integration testing. Testcontainers allows spinning up containers for dependencies such as the
`Postgres database`, providing an isolated and reproducible environment for integration testing.