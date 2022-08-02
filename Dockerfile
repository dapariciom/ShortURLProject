FROM openjdk:18
EXPOSE 8080
ADD target/demo-docker.jar demo-docker.jar
ENTRYPOINT ["java", "-jar", "/demo-docker.jar"]