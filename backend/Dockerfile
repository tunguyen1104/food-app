# First stage: Build the application
FROM gradle:8.5-jdk17-alpine AS build
WORKDIR /app

# Copy everything to the container
COPY . .

# Build the application
RUN gradle build --no-daemon

# Second stage: Run the application
FROM openjdk:17-jdk-alpine

WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8081

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
