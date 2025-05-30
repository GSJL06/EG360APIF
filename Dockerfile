# Multi-stage build for EducaGestor360 API
FROM eclipse-temurin:17-jdk AS build

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build application
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre

# Set working directory
WORKDIR /app

# Create non-root user for security
RUN groupadd -r educagestor && useradd -r -g educagestor educagestor

# Copy JAR file from build stage
COPY --from=build /app/target/educagestor-api-*.jar app.jar

# Change ownership to non-root user
RUN chown educagestor:educagestor app.jar

# Switch to non-root user
USER educagestor

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# Set JVM options for containerized environment
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
