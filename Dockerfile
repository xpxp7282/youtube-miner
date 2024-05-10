# Copyright 2024 (replace with your name/year)
#
# (Add your license information here if applicable)

# Stage 1: Build the Spring Boot application
FROM maven:3.9.5-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml ./
RUN mvn clean package

# Stage 2: Create the final image
FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Expose the port your Spring Boot application runs on (replace with your actual port)
EXPOSE 8082

# Define the command to run your application when the container starts
CMD ["java", "-jar", "app.jar"]
