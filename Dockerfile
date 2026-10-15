# ==========================================
# Multi-stage Dockerfile for Spring Boot
# ==========================================

# 1. Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /build

# Copy Maven descriptor and fetch dependencies (improves caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build production jar
COPY src ./src
RUN mvn clean package -DskipTests

# 2. Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy packaged jar from builder stage
COPY --from=builder /build/target/*.jar app.jar

# Render & Railway supply PORT dynamically
ENV PORT=8081
EXPOSE 8081

# JVM tuning: cap heap at 400m to stay within Render free tier 512MB RAM limit.
# Uses OpenAI API for embeddings (no local ONNX model loaded into heap).
ENTRYPOINT ["java", \
  "-Xmx400m", \
  "-Xms128m", \
  "-XX:+UseContainerSupport", \
  "-Dserver.port=${PORT}", \
  "-jar", "app.jar"]
