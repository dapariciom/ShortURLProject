FROM openjdk:11
EXPOSE 8080
ADD target/demo-url-project.jar demo-url-project.jar
ENTRYPOINT ["java", "-jar", "/demo-url-project.jar"]