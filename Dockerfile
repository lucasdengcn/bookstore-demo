# Multi-stage build for optimized Docker image with AOT, extracted JAR, and CDS
FROM gradle:8.13-jdk21-alpine AS builder

# Set working directory
WORKDIR /app

# Copy gradle files first to leverage Docker cache
COPY gradle gradle
COPY gradlew .
COPY gradlew.bat .
COPY build.gradle .
COPY settings.gradle .
COPY gradle.properties* ./
COPY libs.versions.toml gradle/

# Copy source code
COPY src src

# Build the application with AOT enabled
RUN ./gradlew clean build -x test -Dspring.aot.enabled=true

# Extract the layered JAR
RUN java -Djarmode=tools -jar build/libs/*.jar extract --destination extracted

# CDS preparation stage
FROM eclipse-temurin:21-jdk-alpine AS cds-preparer

WORKDIR /app

# Copy the extracted application
COPY --from=builder /app/extracted/ ./

# Create CDS archive at build time
# First run to generate class list and archive
RUN java -XX:DumpLoadedClassList=classes.lst \
         -Dspring.aot.enabled=true \
         -Dspring.config.additional-location=file:./config/biz-config.yaml \
         -Dspring.context.exit=onRefresh \
         org.springframework.boot.loader.JarLauncher || true

# Create the CDS archive
RUN java -Xshare:dump \
         -XX:SharedClassListFile=classes.lst \
         -XX:SharedArchiveFile=application.jsa \
         org.springframework.boot.loader.JarLauncher || true

# Runtime stage using a lightweight Java 21 image optimized for AOT and CDS
FROM eclipse-temurin:21-jre-alpine

# Install curl for health checks
RUN apk add --no-cache curl

# Create app user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# Set working directory
WORKDIR /app

# Copy the extracted application layers
COPY --from=builder --chown=spring:spring /app/extracted/ ./

# Copy the CDS archive
COPY --from=cds-preparer --chown=spring:spring /app/application.jsa ./

# Copy additional configuration files
COPY --from=builder --chown=spring:spring /app/src/main/resources/application.yaml ./config/
COPY --from=builder --chown=spring:spring /app/dynamic-config/biz-config.yaml ./config/

# Create necessary directories
RUN mkdir -p logs public/images temp

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# JVM options optimized for AOT, CDS, and container performance
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom -Dspring.aot.enabled=true -Dspring.config.additional-location=file:./config/biz-config.yaml -XX:SharedArchiveFile=application.jsa"

# Run the application with CDS and optimized startup
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.JarLauncher"]