# ==========================================
# Multi-stage Dockerfile for Spring Boot
# ==========================================

# 1. Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /build

# Pre-fetch ONNX embedding model & tokenizer for instant container startup
RUN mkdir -p /build/models && \
    curl -sL "https://raw.githubusercontent.com/spring-projects/spring-ai/main/models/spring-ai-transformers/src/main/resources/onnx/all-MiniLM-L6-v2/tokenizer.json" -o /build/models/tokenizer.json && \
    curl -sL "https://media.githubusercontent.com/media/spring-projects/spring-ai/refs/heads/main/models/spring-ai-transformers/src/main/resources/onnx/all-MiniLM-L6-v2/model.onnx" -o /build/models/model.onnx

# Copy Maven descriptor and fetch dependencies (improves caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build production jar
COPY src ./src
RUN mvn clean package -DskipTests

# 2. Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy pre-fetched model and packaged jar from builder stage
COPY --from=builder /build/models ./models
COPY --from=builder /build/target/*.jar app.jar

# Render & Railway supply PORT dynamically
ENV PORT=8081
EXPOSE 8081

# Run the Spring Boot application
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
