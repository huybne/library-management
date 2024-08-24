
FROM maven:4.0.0-openjdk:17 AS build
COPY library-management-system .
RUN mvn clean package -DskipTests


EXPOSE 8082
ARG JAR_FILE=/target/library-management-system-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]

