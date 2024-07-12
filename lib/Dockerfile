FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml .

COPY logger-utils/pom.xml ./logger-utils/
COPY neostudy-lib-spring-boot-starter/pom.xml ./neostudy-lib-spring-boot-starter/


COPY logger-utils/src ./logger-utils/src
COPY neostudy-lib-spring-boot-starter/src ./neostudy-lib-spring-boot-starter/src

RUN mvn clean install

FROM openjdk:17-jdk-slim

COPY --from=build /app/logger-utils/target/logger-utils-1.1-SNAPSHOT.jar /app/lib/logger-utils.jar
COPY --from=build /app/neostudy-lib-spring-boot-starter/target/neostudy-lib-spring-boot-starter-1.1-SNAPSHOT.jar /app/lib/neostudy-lib-spring-boot-starter.jar
