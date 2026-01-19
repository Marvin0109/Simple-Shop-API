# SIMPLE-SHOP-API

SIMPLE-SHOP-API ist eine simple Datenbank REST-API Anwendung, die im Modul *Datenbanken: Weiterführende Konzepte*
als 2-wöchige Abschlussprüfung implementiert werden musste.

## Funktionen

- CRUD für Kunden, Mitarbeiter, Adresse, Produkte, Bestellungen und Bestellpositionen
- Vorhin genannten Entitäten anlegen
- Login Feature (Passwortmanagement **vereinfach**)
- RESTful API (JSON)

## Tech Stack

- **Backend:** Spring Boot
- **Datenbank:** PostgreSQL
- **Containerisierung:** Docker + Docker Compose

## Voraussetzungen

- Docker + Docker Compose
- PostgreSQL Client
- Java 17
- Maven

## Installation und Nutzung (Lokal)

### Linux / macOS

#### 1. Berechtigungen setzen (falls nötig)
```
$ chmod +x start.sh
$ chmod +x clean.sh
```

#### 2. Anwendung starten

```
$ ./start.sh    # startet die PostgreSQL-Datenbank im Docker-Container und danach die Spring Boot API
```

Die API läuft dann unter `http://localhost:8080`.

#### 3. Anwendung testen

Testen der Endpoints sowie normale Nutzung der Anwendung durch Tools wie [Postman](https://www.postman.com/) oder Swagger-UI
(`http://localhost:8080/swagger-ui/index.html`).

#### 4. Anwendung stoppen

- Beenden der Anwendung mit `ctrl+c`

#### 5. Container und Datenbank komplett entfernen

```
$ ./clean.sh    # stoppt den Container, löscht Container & Volumes (Datenbank zurücksetzen)
```

### Docker Hinweise

- Datenbankcontainer läuft persistent im Hintergrund, bis `clean.sh` ausgeführt wird
- Volumen: `./db-data`


### Beispiele für API Endpoints:
- `GET /mitarbeiter` - alle Mitarbeiter abrufen
- `GET /login/kunde` - Login für Kunden
- `DELETE /bestellpositionen` - Bestellpositionen löschen

Weitere Endpoints sind dem [Dokument](src/main/resources/docs/DBSW_WISE2526_Abschlussprojekt_Aufgaben.pdf) ab 
Seite 9 zu entnehmen.