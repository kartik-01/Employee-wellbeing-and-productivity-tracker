FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY springboot-app-prodwell/pom.xml .
COPY springboot-app-prodwell/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]