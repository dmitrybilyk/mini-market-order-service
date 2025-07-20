# Use a lightweight JDK base image
FROM eclipse-temurin:21-jre-alpine

# Set app directory
WORKDIR /app

# Copy the Spring Boot jar to the container
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# Expose the port your app listens on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
