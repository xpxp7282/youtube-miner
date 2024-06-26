# Stage 1: Build the Spring Boot application
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml ./
COPY . .
RUN mvn clean validate compile package

# Stage 2: Create the final image
FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Expose the port your Spring Boot application runs on (replace with your actual port)
EXPOSE 8082

# Define the command to run your application when the container starts
CMD ["java", "-jar", "app.jar"]
