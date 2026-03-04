# SIMPLE-SHOP-API

> **Autor**: Marvin0109,
> **aktualisiert am**: 04.03.2026,
> **Version**: 1.4

SIMPLE-SHOP-API ist eine simple REST-API Anwendung, die im Modul *Datenbanken: Weiterführende Konzepte*
als 2-wöchige Abschlussprüfung implementiert werden musste. Sie verwendet **PostgreSQL** als Datenbank
und bietet grundlegende Funktionen für ein einfaches Shop-System an.

## Funktionen

- CRUD für Kunden, Mitarbeiter, Adresse, Produkte, Bestellungen und Bestellpositionen
- Login Feature (Passwortmanagement **vereinfacht!**)

## Tech Stack

- **Backend:** Spring Boot
- **Datenbank:** PostgreSQL
- **Containerisierung:** Docker + Docker Compose
- **API:** RESTful API (JSON)

## Voraussetzungen

- Docker + Docker Compose
- PostgreSQL Client
- Java 17
- Maven
- Unix System / Windows (nicht getestet)

## Konfigurationen der Datenbankverbindung

Die Anwendung verwendet **JDBC** zur Verbindung mit einer **PostgreSQL-Datenbank**.
Befolgen Sie die Anweisungen in den beiden Dateien `example.env` sowie 
`src/main/resources/application-local-example.properties`, bevor Sie die Anwendung starten.

## Installation und Nutzung (Lokal)

### Linux / macOS

#### 0. Repository klonen (SSH)

```
$ git clone git@github.com:Marvin0109/Simple-Shop-API.git
```

#### 1. Berechtigungen setzen (falls nötig)

```
$ chmod +x start.sh
$ chmod +x clean.sh
```

#### 2. Anwendung starten

```
$ ./start.sh    # startet die PostgreSQL-Datenbank im Docker-Container und danach die Spring Boot API
```

oder

```
$ ./start.sh --clean # Alte Docker-Container und Volumes werden gelöscht, sofern vorhanden
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

Für alle weiteren Endpoints zum Testen, gehe zum Abschnitt *Literatur* ganz unten.

## Code Deployment

### Hinweis

Die Anwendung ist deployable über Railway, jedoch ist die Anwendung mit HTTPS noch nicht funktionsfähig.

## Literatur

Das dieses Projekt ein Abschlussprojekt war, gab es vom Dozenten ein Dokument mit Installationsanweisungen,
Anforderungen, Endpoints, Datenbankaufbau usw.
Dieses Dokument ist [hier](src/main/resources/docs/DBSW_WISE2526_Abschlussprojekt_Aufgaben.pdf) verlinkt.

