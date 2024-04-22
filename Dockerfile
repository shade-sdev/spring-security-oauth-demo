FROM openjdk:17

WORKDIR /app

COPY target/spring-security-oauth-demo-1.0-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
