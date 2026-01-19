#!/bin/bash

echo "Stoppe Docker-Container..."
docker compose down

echo "Optional: Volumes löschen, um die Datenbank komplett zurückzusetzen..."
read -p "Möchtest du die Datenbankdaten löschen? (y/n) " confirm

if [[ "$confirm" == "y" || "$confirm" == "Y" ]]; then
    docker compose down -v
    echo "Volumes wurden gelöscht."
else
    echo "Volumes bleiben erhalten."
fi

echo "Cleanup fertig."
