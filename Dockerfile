# Use OpenJDK 22 as the base image
FROM eclipse-temurin:22-jdk

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/hangMan.jar app.jar

# Expose the port the app runs on (default is 8080 for Spring Boot)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
