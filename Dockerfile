# Dockerfile
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY build/libs/*.war app.war

EXPOSE 8888

CMD ["java", "-jar", "app.war"]
