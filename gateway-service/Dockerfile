FROM openjdk:17-oracle
VOLUME /tmp
COPY target/*.jar  app.jar
ENTRYPOINT ["java","-jar", "app.jar"]


#cd commons && mvn clean install -DskipTests && cd ..
#cd position-service && mvn clean package -DskipTests
