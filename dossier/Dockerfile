FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /app

COPY --from=neostudy-lib /app/lib/logger-utils.jar ./lib/logger-utils.jar

RUN mvn install:install-file  \
    -Dfile=/app/lib/logger-utils.jar  \
    -DgroupId=ru.chainichek.neostudy.lib  \
    -DartifactId=logger-utils  \
    -Dversion=1.1-SNAPSHOT  \
    -Dpackaging=jar

COPY pom.xml ./
COPY src ./src

RUN mvn clean package

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/dossier-0.0.1-SNAPSHOT.jar ./dossier.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "dossier.jar"]