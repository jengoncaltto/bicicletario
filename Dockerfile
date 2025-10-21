FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/aluguel.jar aluguel.jar

EXPOSE 80

ENTRYPOINT ["java", "-jar", "aluguel.jar"]
