# --- Stage 1: Build the application ---
FROM maven:3.8.5-openjdk-17 AS build

# Set working directory
WORKDIR /app

# Copy all source code
COPY . .

# Package the application without running tests
RUN mvn clean package -DskipTests

# --- Stage 2: Run the built JAR ---
FROM openjdk:17.0.1-jdk-slim

# Set working directory
WORKDIR /app

# Copy JAR file from the build stage
COPY --from=build /app/target/HiringWire-0.0.1-SNAPSHOT.jar HiringWire.jar

# Expose the port used by Spring Boot
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "HiringWire.jar"]
