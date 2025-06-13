#FROM openjdk:17-oracle
#VOLUME /tmp
#COPY target/*.jar  app.jar
#ENTRYPOINT ["java","-jar", "app.jar"]


FROM openjdk:17-oracle
VOLUME /tmp

# Copy your application jar
COPY target/*.jar app.jar

# Copy the Keycloak certificate into the image
COPY keycloak.crt /tmp/keycloak.crt

# Import the certificate into JVM truststore
RUN keytool -importcert -noprompt -trustcacerts -alias keycloak \
    -file /tmp/keycloak.crt -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit

# Remove the certificate file after import
RUN rm /tmp/keycloak.crt

ENTRYPOINT ["java", "-jar", "app.jar"]
