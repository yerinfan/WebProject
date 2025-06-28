# 1단계: Gradle 빌드 단계
FROM gradle:8.4.0-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle clean bootWar --no-daemon

# 2단계: 실제 실행 환경
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.war app.war
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.war"]
