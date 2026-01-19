FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Maven installieren
RUN apk add --no-cache maven

COPY pom.xml .
COPY src ./src

# Maven Build
RUN mvn clean package -DskipTests

# --- neues Image für App ---
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# JAR vom Build-Image kopieren
COPY --from=build /app/target/simpleshopapi-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

