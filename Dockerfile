# Step 1: Use an official Tomcat runtime as a parent image
FROM tomcat:10-jdk17-temurin

# Step 2: Remove default webapps to avoid conflicts
RUN rm -rf /usr/local/tomcat/webapps/*

# Step 3: Copy the WAR file into the Tomcat webapps directory
COPY target/hangMan.war /usr/local/tomcat/webapps/ROOT.war

# Step 4: Expose the port that your Tomcat application runs on
EXPOSE 8080