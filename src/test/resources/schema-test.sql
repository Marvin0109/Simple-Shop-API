-- Dieses Schema enthält nur die Tabellen, es wird keine Validierungslogik getestet

CREATE TABLE adresse (
    adresse_id      INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    aktiv           BOOLEAN NOT NULL,
    strasse         VARCHAR(60) NOT NULL,
    hausnummer      VARCHAR(5) NOT NULL,
    plz             VARCHAR(12) NOT NULL,
    ort             TEXT NOT NULL,
    land            TEXT NOT NULL
);

CREATE TABLE kunde (
    kunde_id        INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email           VARCHAR(256) NOT NULL,
    vorname         VARCHAR(32) NOT NULL,
    nachname        VARCHAR(32) NOT NULL,
    passwort        VARCHAR(20) NOT NULL
);

CREATE TABLE kunde_hat_adressen (
    adresse_id      INT NOT NULL,
    kunde_id        INT NOT NULL,
    typ             TEXT NOT NULL,
    PRIMARY KEY (adresse_id, kunde_id),
    FOREIGN KEY (adresse_id) REFERENCES adresse(adresse_id),
    FOREIGN KEY (kunde_id) REFERENCES kunde(kunde_id)
);

CREATE TABLE mitarbeiter (
    personal_nr     INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    vorname         TEXT NOT NULL,
    nachname        TEXT NOT NULL,
    email           TEXT NOT NULL,
    passwort        VARCHAR(20) NOT NULL
);

CREATE TABLE bestellung (
    bestellung_id           INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    datum                   TIMESTAMP NOT NULL,
    status                  VARCHAR(13),
    mitarbeiterzuweis       INT NOT NULL,
    kunde_id                INT NOT NULL,
    FOREIGN KEY (mitarbeiterzuweis) REFERENCES mitarbeiter(personal_nr),
    FOREIGN KEY (kunde_id) REFERENCES kunde(kunde_id)
);

CREATE TABLE produkt (
    sku             TEXT NOT NULL UNIQUE PRIMARY KEY,
    name            TEXT NOT NULL,
    preis           DECIMAL(10, 2) NOT NULL,
    lagerbestand    INT NOT NULL,
    angelegt_von    INT NOT NULL,
    FOREIGN KEY (angelegt_von) REFERENCES mitarbeiter(personal_nr)
);

CREATE TABLE IF NOT EXISTS bestellposition (
    position_id     INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    menge           INT,
    sku             TEXT NOT NULL,
    bestellung_id   INT NOT NULL,
    FOREIGN KEY (sku) REFERENCES produkt(sku),
    FOREIGN KEY (bestellung_id) REFERENCES bestellung(bestellung_id)
);