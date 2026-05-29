FROM eclipse-temurin:24-jdk

# Copiamos el jar generado en target/
COPY target/Parcial-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto de tu aplicación
EXPOSE 8113

# Definimos el entrypoint
ENTRYPOINT ["java", "-jar", "app.jar"]
