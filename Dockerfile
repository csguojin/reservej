FROM maven:3.8.7-openjdk-18 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B package

FROM openjdk:18-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/reserve-0.0.1-SNAPSHOT.jar ./app.jar

CMD ["java", "-jar", "app.jar"]