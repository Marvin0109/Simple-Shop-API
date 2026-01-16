-- Erstellung der Relationen

CREATE TABLE IF NOT EXISTS adresse (
    adresse_id      INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    aktiv           BOOLEAN NOT NULL,
    strasse         VARCHAR(60) NOT NULL CHECK (strasse ~ '^[A-ZÄÖÜ][a-zäöüß]*$'),
    hausnummer      VARCHAR(5) NOT NULL CHECK (hausnummer ~ '^[0-9]+[a-z]?$'),
    plz             VARCHAR(12) NOT NULL CHECK (plz ~ '^[0-9]{1,12}$'),
    ort             TEXT NOT NULL,
    land            TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS kunde (
    kunde_id        INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email           VARCHAR(256) NOT NULL UNIQUE CHECK (email ~ '^[^@]+@[^@]+\.[^@]+$'),
    vorname         VARCHAR(32) NOT NULL CHECK (vorname ~ '^[A-ZÄÖÜ][a-zäöüß]*$'),
    nachname        VARCHAR(32) NOT NULL CHECK (nachname ~ '^[A-ZÄÖÜ][a-zäöüß]*$'),
    passwort        VARCHAR(20) NOT NULL
        CHECK(LENGTH(passwort) BETWEEN 5 AND 20)
        CHECK(passwort ~ '[A-Za-z]')
        CHECK(passwort ~ '[0-9]')
        CHECK(passwort ~ '[^A-Za-z0-9]')
);

CREATE TABLE IF NOT EXISTS kunde_hat_adressen (
    adresse_id      INT NOT NULL,
    kunde_id        INT NOT NULL,
    typ             TEXT NOT NULL CHECK(typ IN ('Lieferadresse', 'Rechnungsadresse')),
    PRIMARY KEY (adresse_id, kunde_id),
    FOREIGN KEY (adresse_id) REFERENCES adresse(adresse_id),
    FOREIGN KEY (kunde_id) REFERENCES kunde(kunde_id)
);

CREATE TABLE IF NOT EXISTS mitarbeiter (
    personal_nr     INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    vorname         TEXT NOT NULL,
    nachname        TEXT NOT NULL,
    email           TEXT NOT NULL,
    passwort        VARCHAR(20) NOT NULL
        CHECK(LENGTH(passwort) BETWEEN 5 AND 20)
        CHECK(passwort ~ '[A-Za-z]')
        CHECK(passwort ~ '[0-9]')
        CHECK(passwort ~ '[^A-Za-z0-9]')
);

CREATE TABLE IF NOT EXISTS bestellung (
    bestellung_id           INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    datum                   TIMESTAMP NOT NULL,
    status                  VARCHAR(13) NOT NULL CHECK(status IN (
                                'neu', 'bezahlt', 'versendet', 'abgeschlossen', 'storniert'
                            )),
    mitarbeiterzuweis       INT NOT NULL,
    kunde_id                INT NOT NULL,
    FOREIGN KEY (mitarbeiterzuweis) REFERENCES mitarbeiter(personal_nr),
    FOREIGN KEY (kunde_id) REFERENCES kunde(kunde_id)
);

CREATE TABLE IF NOT EXISTS produkt (
    sku             TEXT NOT NULL UNIQUE PRIMARY KEY,
    name            TEXT NOT NULL,
    preis           DECIMAL(10, 2) NOT NULL,
    lagerbestand    INT NOT NULL CHECK(lagerbestand >= 0),
    angelegt_von    INT NOT NULL,
    FOREIGN KEY (angelegt_von) REFERENCES mitarbeiter(personal_nr)
);

CREATE TABLE IF NOT EXISTS bestellposition (
    position_id     INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    menge           INT CHECK (menge > 0),
    sku             TEXT NOT NULL,
    bestellung_id   INT NOT NULL,
    FOREIGN KEY (sku) REFERENCES produkt(sku),
    FOREIGN KEY (bestellung_id) REFERENCES bestellung(bestellung_id)
);



-- Implementierung der Triggern

CREATE OR REPLACE FUNCTION check_status_stornierung()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status = 'storniert' AND OLD.status NOT IN ('neu', 'bezahlt') THEN
        RAISE EXCEPTION 'Status darf nur von neu/bezahlt auf storniert geändert werden!';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER StatusStornierung
BEFORE UPDATE ON bestellung
FOR EACH ROW
WHEN (OLD.status IS DISTINCT FROM NEW.status)
EXECUTE FUNCTION check_status_stornierung();

------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION check_lagerbestand_before_insert_bestellung()
RETURNS TRIGGER AS $$
DECLARE
    current_lagerbestand INT;
    bestell_status TEXT;
BEGIN
    SELECT status INTO bestell_status
    FROM bestellung
    WHERE bestellung_id = NEW.bestellung_id;

    IF bestell_status = 'storniert' THEN
        RETURN NEW;
    END IF;

    SELECT lagerbestand INTO current_lagerbestand
    FROM produkt
    WHERE sku = NEW.sku;

    IF current_lagerbestand < NEW.menge THEN
        RAISE EXCEPTION 'Nicht genug Lagerbestand für Produkt %: Lagerbestand = %, Gewünschte Menge = %',
            NEW.sku, current_lagerbestand, NEW.menge;
    END IF;

    UPDATE produkt
    SET lagerbestand = lagerbestand - NEW.menge
    WHERE sku = NEW.sku;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER LagerbestandCheckNachBestellung
BEFORE INSERT ON bestellposition
FOR EACH ROW
EXECUTE FUNCTION check_lagerbestand_before_insert_bestellung();

------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION check_lagerbestand_after_update_bestellmenge()
RETURNS TRIGGER AS $$
DECLARE
    current_lagerbestand INT;
    bestell_status TEXT;
    differenz INT;
BEGIN
    SELECT status INTO bestell_status
    FROM bestellung
    WHERE bestellung_id = NEW.bestellung_id;

    IF bestell_status = 'storniert' THEN
        RETURN NEW;
    END IF;

    differenz := NEW.menge - OLD.menge;

    IF differenz = 0 THEN
        RETURN NEW;
    END IF;

    SELECT lagerbestand INTO current_lagerbestand
    FROM produkt
    WHERE sku = NEW.sku;

    IF differenz > 0 THEN
        IF current_lagerbestand < differenz THEN
            RAISE EXCEPTION
                'Nicht genügend Lagerbestand für SKU %: benötigt %, vorhanden %',
                NEW.sku, differenz, current_lagerbestand;
        END IF;

        UPDATE produkt
        SET lagerbestand = lagerbestand - differenz
        WHERE sku = NEW.sku;
        RETURN NEW;
    END IF;

    UPDATE produkt
    SET lagerbestand = lagerbestand - differenz
    WHERE sku = NEW.sku;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER LagerbestandCheckNachUpdateMenge
BEFORE UPDATE OF menge ON bestellposition
FOR EACH ROW
EXECUTE FUNCTION check_lagerbestand_after_update_bestellmenge();

------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION update_lagerbestand_after_delete_bestellung()
RETURNS TRIGGER AS $$
DECLARE
    bestell_status TEXT;
BEGIN
    SELECT status INTO bestell_status
    FROM bestellung
    WHERE bestellung_id = OLD.bestellung_id;

    IF bestell_status <> 'storniert' THEN
        UPDATE produkt
        SET lagerbestand = lagerbestand + OLD.menge
        WHERE sku = OLD.sku;
    END IF;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER LagerbestandUpdateNachBestellungLöschen
AFTER DELETE ON bestellposition
FOR EACH ROW
EXECUTE FUNCTION update_lagerbestand_after_delete_bestellung();

------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION update_lagerbestand_after_cancel_bestellung()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.status <> 'storniert' AND NEW.status = 'storniert' THEN
        UPDATE produkt p
        SET lagerbestand = p.lagerbestand + bp.menge
        FROM bestellposition bp
        WHERE bp.bestellung_id = NEW.bestellung_id AND bp.sku = p.sku;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER LagerbestandUpdateNachBestellungStornieren
AFTER UPDATE OF status ON bestellung
FOR EACH ROW
EXECUTE FUNCTION update_lagerbestand_after_cancel_bestellung();



-- Implementierung der Views

CREATE OR REPLACE VIEW v_kunde_summe_anzahl_bestellungen AS
SELECT
    k.kunde_id,
    k.email,
    COUNT(DISTINCT b.bestellung_id) AS anzahl_bestellungen,
    COALESCE(SUM(bp.menge * p.preis), 0) AS gesamtsumme
FROM
    Kunde k
LEFT JOIN bestellung b
    on k.kunde_id = b.kunde_id
LEFT JOIN bestellposition bp
    on b.bestellung_id = bp.bestellung_id
LEFT JOIN produkt p
    on bp.sku = p.sku
GROUP BY
    k.kunde_id,
    k.email
ORDER BY
    k.kunde_id;

------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE VIEW v_produkt_verkaufszahlen AS
SELECT
    p.sku,
    p.name,
    COALESCE(SUM(CASE
        WHEN b.status NOT IN ('neu', 'storniert')
            THEN bp.menge
        ELSE 0
        END
            ), 0
    ) AS gesamt_verkaufte_menge,
    COALESCE(SUM(CASE
        WHEN b.status NOT IN ('neu', 'storniert')
            THEN bp.menge * p.preis
        ELSE 0
        END
            ), 0
    ) AS umsatz,
    COUNT(DISTINCT CASE
        WHEN b.status NOT IN ('neu', 'storniert')
            THEN b.bestellung_id
        END
    ) AS anzahl_bestellungen
FROM
    produkt p
LEFT JOIN bestellposition bp
  ON p.sku = bp.sku
LEFT JOIN bestellung b
  ON bp.bestellung_id = b.bestellung_id
GROUP BY
    p.sku,
    p.name
ORDER BY
    gesamt_verkaufte_menge DESC;

------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE VIEW v_mitarbeiter_uebersicht AS
SELECT
    m.personal_nr,
    COALESCE(b.anzahl_bestellungen, 0) AS anzahl_verwalteter_bestellungen,
    COALESCE(p.anzahl_produkte, 0) AS anzahl_angelegter_produkte
FROM mitarbeiter m
LEFT JOIN (
    SELECT mitarbeiterzuweis, COUNT(*) AS anzahl_bestellungen
    FROM bestellung
    GROUP BY mitarbeiterzuweis
) b ON m.personal_nr = b.mitarbeiterzuweis
LEFT JOIN (
    SELECT angelegt_von, COUNT(*) AS anzahl_produkte
    FROM produkt
    GROUP BY angelegt_von
) p ON m.personal_nr = p.angelegt_von
ORDER BY m.personal_nr;

------------------------------------------------------------------------------------------------------------------------

CREATE OR REPLACE VIEW v_mitarbeiter_bestellstatus_uebersicht AS
SELECT
    m.personal_nr,
    s.status,
    COALESCE(b.anzahl_bestellungen, 0) AS anzahl_bestellungen
FROM mitarbeiter m
    CROSS JOIN (
        SELECT 'neu' AS status
        UNION ALL
        SELECT 'bezahlt'
        UNION ALL
        SELECT 'versendet'
        UNION ALL
        SELECT 'storniert'
        UNION ALL
        SELECT 'abgeschlossen'
    ) s
LEFT JOIN (
    SELECT
        mitarbeiterzuweis,
        status,
        COUNT(*) AS anzahl_bestellungen
    FROM bestellung
    GROUP BY
        mitarbeiterzuweis,
        status
) b
    ON m.personal_nr = b.mitarbeiterzuweis AND s.status = b.status
ORDER BY
    m.personal_nr,
    s.status;



