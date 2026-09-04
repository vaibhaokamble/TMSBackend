# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create the production image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built jar from the build stage
# The wildcard *.jar matches the taskManagement-0.0.1-SNAPSHOT.jar (or whatever it's named)
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 for the application
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
