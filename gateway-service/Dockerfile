#FROM openjdk:17-oracle
#VOLUME /tmp
#COPY target/*.jar  app.jar
#ENTRYPOINT ["java","-jar", "app.jar"]

# -------- Stage 1: Build the application with Maven --------
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy all files (including parent and modules)
COPY . .

# Download dependencies (go offline) for the whole project
RUN mvn dependency:go-offline

# Package the app (replace 'config-service' with your module name if needed)
RUN mvn clean package -DskipTests -pl config-service -am

# -------- Stage 2: Run the packaged app with OpenJDK --------
FROM openjdk:17-oracle
VOLUME /tmp
WORKDIR /app

# Copy the jar of the module from the builder
COPY --from=builder /app/config-service/target/*.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
