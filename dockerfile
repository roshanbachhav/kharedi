# Stage 1: Build the application
FROM eclipse-temurin:11-jdk as builder

COPY . .

RUN mvn clean package -DskipTests

COPY --from=build /usr/src/app/target/kharedi-0.0.1-SNAPSHOT.jar /kharedi.jar

# Expose the port
EXPOSE 5050

# Command to run the application
ENTRYPOINT ["java", "-jar", "/kharedi.jar"]
