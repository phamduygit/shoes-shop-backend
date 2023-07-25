FROM eclipse-temurin:17

LABEL mentainer="javaguides.net@gmail.com"

WORKDIR /app

COPY target/back-end-0.0.1-SNAPSHOT.jar /app/back-end-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "back-end-0.0.1-SNAPSHOT.jar"]