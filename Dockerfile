# Multi-stage build for SICUA Backend
FROM openjdk:17-jdk-slim as build

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (cached layer)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Production stage
FROM openjdk:17-jre-slim

# Set working directory
WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/target/store-sicua-back-0.0.1-SNAPSHOT.jar app.jar

# Create non-root user for security
RUN groupadd -r sicua && useradd -r -g sicua sicua
RUN chown -R sicua:sicua /app
USER sicua

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/store-config || exit 1

# JVM options for container environment
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseContainerSupport -Djava.security.egd=file:/dev/./urandom"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=$PORT -jar app.jar"]
