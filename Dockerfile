# Utiliser une image Maven pour construire le projet
FROM maven:3.8.6-openjdk-11-slim AS builder

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier pom.xml et le code source Java
COPY pom.xml .
COPY src/capteurtempjava /app/capteurtempjava

# Installer les dépendances et compiler le projet
RUN mvn clean package

# Étape pour exécuter l'application
FROM openjdk:11-jre-slim

COPY --from=builder /app/target/*.jar /app/capteurtemp.jar

# Exécuter l'application
CMD ["java", "-jar", "/app/capteurtemp.jar"]
