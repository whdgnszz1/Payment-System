# Runtime stage only
FROM openjdk:17-jdk-slim

VOLUME /tmp

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy the pre-built jar file
COPY build/libs/*.jar app.jar

# Create non-root user
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "/app.jar"] 