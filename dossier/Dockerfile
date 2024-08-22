FROM bellsoft/liberica-openjre-alpine:21.0.1-12

WORKDIR /app

COPY ./target/dossier.jar ./dossier.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "dossier.jar"]