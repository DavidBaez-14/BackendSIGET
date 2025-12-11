# Etapa 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar solo pom.xml primero (para cachear dependencias)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Runtime (imagen final ligera)
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Crear usuario no-root por seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar JAR desde etapa de build
COPY --from=build /app/target/ProyectosDeGradoUFPS-0.0.1-SNAPSHOT.jar app.jar

# Exponer puerto
EXPOSE 8080

# Variables de entorno con valores por defecto
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SERVER_PORT=8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]