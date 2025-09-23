# =========================
# FASE 1: BUILD
# =========================
FROM maven:3.9.9-eclipse-temurin-22-alpine AS build
# Consider updating to the latest patch version or a more secure tag if available
# Example (replace with the latest secure version after checking Docker Hub):
# FROM maven:3.9.9-eclipse-temurin-22-jammy-20240605 AS build

# Imposta la directory di lavoro
WORKDIR /app

# Copia pom.xml e scarica le dipendenze (per caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia il codice sorgente
COPY src ./src

# Build progetto e genera il jar
RUN mvn clean package -DskipTests

# =========================
# FASE 2: RUNTIME
# =========================
# Fase di runtime
FROM eclipse-temurin:22-jdk-alpine

WORKDIR /app

# Copia jar compilato dalla fase build
COPY --from=build /app/target/university-management-system-0.0.1-SNAPSHOT.jar app.jar

# Espone la porta dell'applicazione
EXPOSE 8081

# Variabili di ambiente per connessione al DB (modifica se vuoi)
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://127.0.0.1:5433/university
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=brant

# Avvia l'applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]
# End of recent edits