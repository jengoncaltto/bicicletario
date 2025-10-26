FROM maven:3.9.6-eclipse-temurin-18-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY src /app/src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/equipamentos.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]