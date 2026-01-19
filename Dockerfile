# --- Build Stage ---
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Maven installieren
RUN apk add --no-cache maven bash git

# Alles kopieren
COPY . .

# Build ohne Tests
RUN mvn clean package -DskipTests

# --- Runtime Stage ---
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# JAR vom Build-Image kopieren
COPY --from=build /target/*.jar app.jar

# Spring Boot muss auf 0.0.0.0 hören
ENV JAVA_OPTS=""
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080

EXPOSE 8080

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
