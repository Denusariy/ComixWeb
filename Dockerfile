FROM openjdk:19
COPY ./target/Comix-0.0.1-SNAPSHOT.jar .
#CMD ["java", "-jar",comix_rest_api-0.0.1-SNAPSHOT.jar "comix_rest_api-0.0.1-SNAPSHOT.jar"]