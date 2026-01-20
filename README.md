# SIMPLE-SHOP-API

SIMPLE-SHOP-API ist eine simple Datenbank REST-API Anwendung, die im Modul *Datenbanken: Weiterführende Konzepte*
als 2-wöchige Abschlussprüfung implementiert werden musste.

## Funktionen

- CRUD für Kunden, Mitarbeiter, Adresse, Produkte, Bestellungen und Bestellpositionen
- Online Shop Logik
- Login Feature (Passwortmanagement **vereinfacht!**)
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
- Unix System

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

Für alle weiteren Endpoints zum Testen, gehe zum Abschnitt *Literatur* ganz unten.

## Öffentlich über Railway

### Hinweis

Die Anwendung ist deployable über Railway, da aber im Rahmen des Informatikstudiums als auch
im Modul DB2 lag der Fokus auf grundlegende Konzepte wie z.B. Funktionsweise von **Schnittstellen (APIs)**, 
**Basisprinzipien der Webentwicklung**, **Codequalität**, **Best Practices**, **Softwarearchitektur** usw. Also diese Themen wurden behandelt, um eine solide Grundlage für die Arbeit
an Webanwendungen zu schaffen.

Themen wie **Sicherheit** (HTTPS, sichere Passwortspeicherung, Authentifizierung von Scratch aus statt über z.B. OAuth)
wurden im Studium nur in einem grundlegenden Rahmen angesprochen und waren in den Projekten und Klausuren nicht der
zentrale Fokus. Sicherheit war eher ein optionales *Upgrade*.

Da diese Themen in den meisten meiner Projekte und Klausuren keine zentrale Rolle spielten, 
hatte ich bisher nicht viel Zeit und Gelegenheit, mich tiefgehend mit ihnen auseinanderzusetzen.

### Literatur

Das dieses Projekt ein Abschlussprojekt war, gab es vom Dozenten ein Dokument mit Installationsanweisungen,
Anforderungen, Endpoints, Datenbankaufbau usw.
Dieses [Dokument](src/main/resources/docs/DBSW_WISE2526_Abschlussprojekt_Aufgaben.pdf) ist hier verlinkt.

