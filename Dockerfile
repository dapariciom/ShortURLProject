FROM openjdk:11
EXPOSE 8080
ADD target/shorten-url-service.jar shorten-url-service.jar
ENTRYPOINT ["java", "-jar", "/shorten-url-service.jar"]