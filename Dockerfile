FROM openjdk:19
COPY ./target/Comix-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "Comix-0.0.1-SNAPSHOT.jar"]