FROM maven:3.9.4-eclipse-temurin-21

WORKDIR /app

# Copiar pom.xml primero para caché de dependencias
COPY pom.xml ./

# Opcional: copiar mvnw y .mvn si existen
# COPY mvnw ./
# COPY .mvn ./

# Descargar todas las dependencias para caché
RUN mvn dependency:go-offline -B

# Copiar el código fuente completo
COPY src ./src

# Construir el paquete sin tests
RUN mvn clean package -DskipTests

# Ejecutar la app (ajusta el nombre del JAR)
CMD ["java", "-jar", "target/intolerables-0.0.1-SNAPSHOT.jar"]
