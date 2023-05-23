FROM openjdk:17-oracle

WORKDIR /opt

COPY target/department-api-0.0.1-SNAPSHOT.jar /opt/department-api.jar

ENTRYPOINT ["java", "-jar", "department-api.jar" ]