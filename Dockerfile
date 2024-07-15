# Dockerfile für Spring Boot Backend mit Eclipse Temurin 17
FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY target/thegame-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]