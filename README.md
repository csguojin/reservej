# Reserve

This is a simple reservation system.
The system supports multiple functions such as user management, resource management, and reservation management.

## Getting Started

Clone the project:

```shell
git clone https://github.com/csguojin/reservej
```

Configure the MySQL and Redis connection and other parameters:

```shell
vim ./src/main/resources/application.yml
```

Build the application:

```shell
mvn -B package
```

Start the application:

```shell
java -jar ./target/reserve-0.0.1-SNAPSHOT.jar
```

If you are using Docker, you can build the Docker image for the project:

```shell
docker build -t reserve:latest .
docker run -p 8080:8080 reserve:latest
```

If you are using Docker Compose, you can easily deploy this project:

```shell
docker compose up -d
```
