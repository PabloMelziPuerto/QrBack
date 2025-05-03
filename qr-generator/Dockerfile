# Etapa 1: Compilación
FROM maven:3.9.4-eclipse-temurin-20 AS build
WORKDIR /qr-generator_qr-generator
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:20-jdk
WORKDIR /app
COPY --from=build /qr-generator_qr-generator/target/*.jar qr-generator_qr-generator.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "qr-generator_qr-generator.jar"]
