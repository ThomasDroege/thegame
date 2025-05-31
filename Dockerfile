# Dockerfile für Spring Boot Backend mit Eclipse Temurin 17
FROM eclipse-temurin:21-jre
VOLUME /tmp
COPY target/thegame-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]