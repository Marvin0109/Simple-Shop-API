#!/bin/bash

# Flags
CLEAN=false
PERSISTENT=false
BUILD_JAR=false

# Help
function show_help() {
  echo "Usage: $0 [options]"
  echo
  echo "Options:"
  echo "  --help        Display options"
  echo "  --clean       Delete old container and volumes"
  echo "  --persistent  Start container with volume"
  echo "  --build       Build/Rebuild JAR-File"
  exit 0
}

for arg in "$@"; do
  case $arg in
    --help)
      show_help
      ;;
    --clean)
      CLEAN=true
      shift
      ;;
    --persistent)
      PERSISTENT=true
      shift
      ;;
    --build)
      BUILD_JAR=true
      shift
      ;;
    *)
      echo "Unknown flag: $arg"
      echo "Try using --help for options"
      ;;
    esac
  done

# Choose compose file
if $PERSISTENT; then
  COMPOSE_FILE="docker-compose-persistent.yml"
  echo "Starting persistent container..."
else
  COMPOSE_FILE="docker-compose.yml"
  echo "Starting non-persistent container..."
fi

# Deleting old container and volumes
if $CLEAN; then
  echo "Old container and volumes being deleted..."
  docker compose -f "$COMPOSE_FILE" down -v
fi

# Starting container
docker compose -f "$COMPOSE_FILE" up -d

# Build / Rebuild jar file

if $BUILD_JAR; then
  echo "Building jar file..."
  mvn clean package
fi

# Check for existing jar file
JAR_FILE=target/simpleshopapi-0.0.1-SNAPSHOT.jar
if [ ! -f "$JAR_FILE" ]; then
  echo "JAR-File not found! Run with option:"
  echo " --build"
  exit 1
fi

# Starting application
echo "Starting Spring Boot App..."
java -jar -Dspring.profiles.active=local "$JAR_FILE"

