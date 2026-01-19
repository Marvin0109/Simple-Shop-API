#!/bin/bash

# Optionale Variable: sauber starten (alte DB löschen)
CLEAN=false
if [ "$1" == "--clean" ]; then
  CLEAN=true
fi

if $CLEAN; then
  echo "Alte Docker-Container und Volumes werden gelöscht..."
  docker compose down -v
fi

echo "Starte Datenbank (Docker)..."
docker compose up -d

# Prüfen, ob die JAR existiert
JAR_FILE=target/simpleshopapi-0.0.1-SNAPSHOT.jar
if [ ! -f "$JAR_FILE" ]; then
  echo "JAR-Datei nicht gefunden! Baue zuerst mit Maven:"
  echo "  mvn clean package"
  exit 1
fi

echo "Starte Spring Boot App..."
java -jar "$JAR_FILE"

