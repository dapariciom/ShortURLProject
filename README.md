# ShortURLProject

##To run the project with Docker Compose

Clean previous .jar versions and build project with maven
```
mvn clean install
```

Build shorten-url-service image
```
docker build -t shorten-url-service:1.0 .
```

Navigate to resources folder
```
shorten-url-service\src\main\resources
```

Run docker compose
```
docker-compose up
```

Enjoy :blush:

