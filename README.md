# thegame
## Installation

1. Add your postgres Credentials (```spring.datasource.username``` and ```spring.datasource.password```) in the ```application.properties file```
2. Populate your local database with execution of  ```thegame.ddl``` script
3. Start the backend with execution of ```ThegameApplication.java```
4. Start the frontend [thegame-frontend](https://github.com/ThomasDroege/thegame-frontend) react app and navigate to localhost:3000


## Techstack:

SpringBoot, Postgres, Thymeleaf, HTML Canvas

Business Package: enthält die Entitäten, welche die DB Struktur darstellt

UI Package: Funktionalitäten, welche in der UI zur Verfügung gestellt werden, gruppiert nach den Masken

# Historie:
Stand 13.05.2023
- Datenbankanbindung für Resourcenanzeige in Village
![image](https://github.com/ThomasDroege/thegame/assets/25778177/b0024332-e8f5-42f5-a6bf-3ee4be45a274)


Notizen für docker Einrichtung:
1. login: docker login
2. docker run --name some-postgres -p:5432:5432 -e POSTGRES_PASSWORD=mysecretpassword -d postgres 
3. docker ps : shows all images which are running
4. kann in DBeaver angeschaut werden 
5. ToDo: DockerFile erstellen (benötigte Dependencies: Java, Postgres, Ausführen des thegame.ddl Skripts)