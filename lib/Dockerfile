FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /app

COPY logger-utils/pom.xml .
RUN mvn dependency:go-offline

COPY logger-utils/src ./src

RUN mvn clean install

FROM openjdk:17-jdk-slim

COPY --from=build /app/target/logger-utils-1.1-SNAPSHOT.jar /app/lib/logger-utils.jar
