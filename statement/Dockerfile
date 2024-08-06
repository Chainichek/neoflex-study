FROM bellsoft/liberica-openjre-alpine:21.0.1-12

WORKDIR /app

COPY ./target/statement.jar ./statement.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "statement.jar"]