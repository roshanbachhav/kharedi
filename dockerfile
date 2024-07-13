FROM openjdk:8-jdk-alpine
COPY . .
RUN mvn clean package -DskipTests

FROM maven:3-openjdk-8-slim
COPY --from=build /target/kharedi-0.0.1-SNAPSHOT.jar kharedi.jar

EXPOSE 5050
ENTRYPOINT ["java","-jar","kharedi.jar"]