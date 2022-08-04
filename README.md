# ShortURLProject

##To run the project with Docker

Clean previous .jar versions and build project with maven
```
mvn clean install
```

Create a docker image with the generated .jar
```
docker build -t demo-docker.jar .
```

Run docker image
```
docker run -p 9090:8080 demo-docker.jar
```

Open a web browser on
```
http://localhost:9090/
```

Enjoy :blush:

