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

Build mongoDB image
```
docker pull mongo:latest

docker run -d -p 27017:27027 --name mongodb-container mongo:latest
```

Run docker compose
```
docker-compose up
```

Enjoy :blush:

