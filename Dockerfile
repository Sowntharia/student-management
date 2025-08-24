FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY src ./src

RUN mvn clean package

CMD mvn spring-boot:run

