FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/gateway-1.0.0-SNAPSHOT.jar ./gateway.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "gateway.jar"]