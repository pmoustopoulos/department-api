# Department API - Spring Boot 3


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


## Start MySQL as a Docker container.
Start `Docker Desktop` and then execute the following command to start a docker container which will be running MySQL <br>

```shell
docker run -p 3306:3306 -d --name my-sql-db -e MYSQL_ROOT_PASSWORD=pass -e MYSQL_DATABASE=mydb mysql
```

## Start the application
```shell
mvn spring-boot:run
```

## Swagger
Swagger was set on the root path, and you can access it on this URL: http://localhost:8080/

