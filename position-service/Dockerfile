#FROM openjdk:17-oracle
#VOLUME /tmp
#COPY target/*.jar  app.jar
#ENTRYPOINT ["java","-jar", "app.jar"]


# -------- Stage 1: Build the application with Maven --------
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy only the Maven project file first and resolve dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY . .

# Package the app
RUN mvn clean package -DskipTests

# -------- Stage 2: Run the packaged app with OpenJDK --------
FROM openjdk:17-oracle
VOLUME /tmp
WORKDIR /app

# Copy the packaged jar from the builder image
COPY --from=builder /app/target/*.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]


#cd commons && mvn clean install -DskipTests && cd ..
#cd position-service && mvn clean package -DskipTests
