FROM eclipse-temurin:17-jre

WORKDIR /app

COPY controller-1.0-SNAPSHOT.jar app.jar

EXPOSE 5173

ENTRYPOINT ["java", "-jar", "app.jar"]