INSERT INTO mitarbeiter (vorname, nachname, email, passwort)
VALUES ('Max', 'Mustermann', 'max@test.de', 'pw123!');

INSERT INTO produkt (sku, name, preis, lagerbestand, angelegt_von)
VALUES ('SKU123', 'Testprodukt', 10.5, 100, 1);

INSERT INTO kunde (email, vorname, nachname, passwort)
VALUES ('kunde@test.de', 'John', 'Doe', 'a#789!');

INSERT INTO bestellung (datum, status, mitarbeiterzuweis, kunde_id)
VALUES (NOW(), 'neu', 1, 1);

