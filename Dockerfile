# 1단계: Java 기반 이미지 사용
FROM eclipse-temurin:21-jdk

# 2단계: WAR 파일 복사
COPY build/libs/*.war /app/app.war

# 3단계: 실행 명령
ENTRYPOINT ["java", "-jar", "/app/app.war"]
