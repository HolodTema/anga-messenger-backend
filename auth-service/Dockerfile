FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY build/libs/auth-service-all.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

