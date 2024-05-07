# Use an OpenJDK base image for Java 17.0.11 from Amazon Corretto
FROM amazoncorretto:17.0.11-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged Spring Boot application JAR file into the container
COPY target/youtubeMiner-0.0.1-SNAPSHOT.jar /app/youtubeMiner-0.0.1-SNAPSHOT.jar

# Expose the port that your Spring Boot application runs on
EXPOSE 8082

# Define the command to run your application when the container starts
CMD ["java", "-jar", "youtubeMiner-0.0.1-SNAPSHOT.jar"]
