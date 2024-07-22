FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /app

COPY --from=neostudy_lib /app/lib/logger-utils.jar ./lib/logger-utils.jar
COPY --from=neostudy_lib /app/lib/neostudy-lib-spring-boot-starter.jar ./lib/neostudy-lib-spring-boot-starter.jar

RUN mvn install:install-file  \
    -Dfile=/app/lib/logger-utils.jar  \
    -DgroupId=ru.chainichek.neostudy.lib  \
    -DartifactId=logger-utils  \
    -Dversion=1.1-SNAPSHOT  \
    -Dpackaging=jar
RUN mvn install:install-file  \
    -Dfile=/app/lib/neostudy-lib-spring-boot-starter.jar  \
    -DgroupId=ru.chainichek.neostudy.lib  \
    -DartifactId=neostudy-lib-spring-boot-starter  \
    -Dversion=1.1-SNAPSHOT  \
    -Dpackaging=jar

COPY pom.xml ./
COPY src ./src

RUN mvn clean package

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/deal-1.2.0-SNAPSHOT.jar ./deal.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "deal.jar"]