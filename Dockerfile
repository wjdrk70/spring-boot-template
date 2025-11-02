# --- 1단계: 빌드(Builder) ---
# Java 17 JDK 이미지를 사용하여 Gradle 빌드를 수행합니다 (gradle.properties 기준)
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Gradle 래퍼 파일 복사
COPY gradlew .
COPY gradle ./gradle

# 빌드 설정 파일 복사
COPY build.gradle .
COPY settings.gradle .
COPY gradle.properties .

# gradlew 실행 권한 부여
RUN chmod +x ./gradlew

# (선택 사항: 의존성을 먼저 다운로드하여 Docker 캐시 활용)
# RUN ./gradlew dependencies

# 전체 소스 코드 복사
COPY . .

# 'core:core-api' 모듈의 실행 가능한 JAR 파일 빌드
#
RUN ./gradlew :core:core-api:bootJar

# --- 2단계: 최종 이미지(Final Image) ---
# 더 작은 JRE(실행 환경) 이미지 사용
FROM eclipse-temurin:17-jre

WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일을 app.jar로 복사
COPY --from=builder /app/core/core-api/build/libs/*.jar app.jar


EXPOSE 8080

# 애플리케이션 실행
CMD ["java", "-jar", "app.jar"]