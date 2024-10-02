# Step 1: Use an official Tomcat runtime as a parent image
FROM tomcat:10-jdk17-temurin

# Step 2: Remove default webapps to avoid conflicts
RUN rm -rf /usr/local/tomcat/webapps/*

# Step 3: Copy the WAR file into the Tomcat webapps directory
COPY target/hangMan.war /usr/local/tomcat/webapps/ROOT.war

# Step 4: Expose the port that your Tomcat application runs on
EXPOSE 8080





# Step 1: Use an official OpenJDK runtime as a parent image
#FROM openjdk:17-jdk-alpine

# Step 2: Set the working directory in the container
#WORKDIR /app

# Step 3: Copy the packaged WAR (or JAR) file into the container
#COPY target/hangMan.war /app/hangMan.war

# Step 4: Expose the port that your Spring Boot application runs on
#EXPOSE 8080

# Step 5: Run the Spring Boot application
#ENTRYPOINT ["java", "-jar", "/app/hangMan.war"]

