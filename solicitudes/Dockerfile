# --- ETAPA 1 ---
# Usamos java21 y mvn para compilar
FROM maven:3-eclipse-temurin-21 AS build

# Creamos una carpeta de trabajo
WORKDIR /app

# Copiamos el pom.xml y descargamos las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos todo lo otro
COPY src ./src

# Compilamos el proyecto y copiamos el .jar
RUN mvn package -DskipTests

# --- ETAPA 2 ---
# Corremos con java21
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiamos SOLO el .jar que se creó en la Etapa 1
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8180

# Comando para arrancar la aplicación
CMD ["java", "-jar", "app.jar"]

# Listo, en la etapa 1 hacemos q el dockerfile compile todo y en la etapa 2 se copia el 
# .jar generado y arranca la aplicacion