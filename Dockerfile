FROM maven:3.9-eclipse-temurin-17

WORKDIR /app

# Resolve dependencies first so this layer is cached between code changes
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY . .

CMD ["mvn", "-B", "clean", "test"]
