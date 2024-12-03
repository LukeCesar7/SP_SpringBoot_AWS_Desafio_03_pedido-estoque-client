# Use an official JDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/msestoque-0.0.1-SNAPSHOT.jar msestoque.jar

# Expose port 8081
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "msestoque.jar"]