# Use an official Maven image as a base image
FROM maven:3.8.4-openjdk-11-slim

# Set the working directory to the project root
WORKDIR /app

# Copy the source code to the container
COPY . /app

# Build the application with Maven
RUN mvn clean install

# Specify the command to run on container start
CMD ["java", "-jar", "target/erp-0.0.1-SNAPSHOT.jar"]
