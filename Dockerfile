# Étape 1 : Utiliser une image JDK pour compiler le code (optionnel si vous construisez le JAR en dehors de Docker)
FROM maven:3.9.9-amazoncorretto-17 AS builder

# Copier les sources dans le conteneur
WORKDIR /app
COPY . .

# Construire l'application Spring Boot
RUN mvn clean package -DskipTests

# Étape 2 : Utiliser une image JRE pour exécuter l'application
FROM amazoncorretto:17.0.0-alpine3.14

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR généré par Maven depuis l'étape précédente
COPY --from=builder /app/target/*.jar app.jar

# Exposer le port utilisé par l'application Spring Boot
EXPOSE 8080

# Commande pour exécuter l'application
CMD ["java", "-jar", "app.jar"]
