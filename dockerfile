# Stage 1: Build the application
FROM maven:3.8.6-openjdk-8 AS build

# Copy the project files
COPY . /usr/src/app

# Set the working directory
WORKDIR /usr/src/app

# Build the application, skipping tests
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:8-jdk-alpine

# Copy the JAR file from the build stage
COPY --from=build /usr/src/app/target/kharedi-0.0.1-SNAPSHOT.jar /kharedi.jar

# Expose the port
EXPOSE 5050

# Command to run the application
ENTRYPOINT ["java", "-jar", "/kharedi.jar"]
