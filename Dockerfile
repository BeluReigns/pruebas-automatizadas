FROM maven:3.9.11-eclipse-temurin-21

WORKDIR /app

COPY .mvn/ .mvn/
COPY pom.xml .
COPY src/ src/

CMD ["mvn", "-B", "test"]