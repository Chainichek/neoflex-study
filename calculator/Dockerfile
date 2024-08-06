FROM bellsoft/liberica-openjre-alpine:21.0.1-12

WORKDIR /app

COPY ./target/calculator.jar ./calculator.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "calculator.jar"]