FROM openjdk:11
VOLUME /tmp
ADD maven/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker","-jar","/app.jar"]
EXPOSE 8081