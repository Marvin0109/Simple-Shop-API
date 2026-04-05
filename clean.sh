#!/bin/bash

# Flags
REMOVE_VOLUMES=false
REMOVE_JAR=false

# Help
function show_help() {
  echo "Usage: $0 [options]"
  echo
  echo "Options:"
  echo "  --help        Display options"
  echo "  --reset       Reset database (deleting volumes)"
  echo "  --remove      Remove built files"
  exit 0
}

for arg in "$@"; do
  case $arg in
    --help)
      show_help
      ;;
    --reset)
      REMOVE_VOLUMES=true
      shift
      ;;
    --remove)
      REMOVE_JAR=true
      shift
      ;;
    *)
      echo "Unknown flag: $arg"
      echo "Try using --help for options"
      exit 1
      ;;
  esac
done

echo "Stopping container..."

if $REMOVE_VOLUMES; then
  docker compose down -v
  echo "Container down and volumes deleted."
else
    docker compose down
    echo "Container down. Volumes remain."
fi

if $REMOVE_JAR; then
  echo "Removing built files..."
  ./mvnw clean
fi

echo "Cleanup finished."
