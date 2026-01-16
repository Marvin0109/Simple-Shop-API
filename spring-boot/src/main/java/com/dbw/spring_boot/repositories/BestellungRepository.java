package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Bestellung;
import com.dbw.spring_boot.model.PositionenFuerBestellung;
import com.dbw.spring_boot.model.Produkt;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.util.*;

@Repository
public class BestellungRepository {

    private final JdbcTemplate jdbcTemplate;

    public BestellungRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ArrayList<Bestellung> findAll() {
        String sql = """
        SELECT
            b.bestellung_id AS bestellung_id,
            b.kunde_id AS kunde_id,
            b.mitarbeiterzuweis AS mitarbeiterzuweis,
            b.datum AS datum,
            b.status AS status,
            bp.bestellung_id AS bestellung_id,
            bp.position_id AS position_id,
            p.sku AS sku,
            p.name AS name,
            p.preis AS preis,
            p.lagerbestand AS lagerbestand,
            p.angelegt_von AS angelegt_von,
            bp.menge AS menge,
            bp.menge * p.preis AS gesamtpreis
        FROM bestellung b
        JOIN bestellposition bp ON bp.bestellung_id = b.bestellung_id
        JOIN produkt p ON bp.sku = p.sku
        """;

        return jdbcTemplate.query(
            sql,
            rs -> {
            Map<Integer, Bestellung> bestellungen = new LinkedHashMap<>();

            while (rs.next()) {
                int bestellungId = rs.getInt("bestellung_id");

                Bestellung bestellung = bestellungen.get(bestellungId);
                if (bestellung == null) {
                      bestellung = new Bestellung();
                      bestellung.setBestellungId(bestellungId);
                      bestellung.setKundeId(rs.getInt("kunde_id"));
                      bestellung.setPersonalNr(rs.getInt("mitarbeiterzuweis"));

                      bestellung.setDatum(rs.getTimestamp("datum")
                            .toInstant()
                            .atOffset(ZoneOffset.UTC)
                      );

                      bestellung.setStatus(rs.getString("status"));

                      bestellungen.put(bestellungId, bestellung);
                }

                produkteForBestellungen(rs, bestellung);
            }

            return new ArrayList<>(bestellungen.values());
        });
    }

    public Optional<Bestellung> findById(int bestellungId) {
        String sql = """
        SELECT
            b.bestellung_id AS bestellung_id,
            b.kunde_id AS kunde_id,
            b.mitarbeiterzuweis AS mitarbeiterzuweis,
            b.datum AS datum,
            b.status AS status,
            bp.bestellung_id AS bestellung_id,
            bp.position_id AS position_id,
            p.sku AS sku,
            p.name AS name,
            p.preis AS preis,
            p.lagerbestand AS lagerbestand,
            p.angelegt_von AS angelegt_von,
            bp.menge AS menge,
            bp.menge * p.preis AS gesamtpreis
        FROM bestellung b
        JOIN bestellposition bp ON bp.bestellung_id = b.bestellung_id
        JOIN produkt p ON bp.sku = p.sku
        WHERE b.bestellung_id = ?
        """;

        return jdbcTemplate.query(
            sql,
            rs -> {
                Bestellung bestellung = null;
                while (rs.next()) {
                    if (bestellung == null) {
                        bestellung = new Bestellung();
                        bestellung.setBestellungId(rs.getInt("bestellung_id"));
                        bestellung.setKundeId(rs.getInt("kunde_id"));
                        bestellung.setPersonalNr(rs.getInt("mitarbeiterzuweis"));

                        bestellung.setDatum(rs.getTimestamp("datum")
                                .toInstant()
                                .atOffset(ZoneOffset.UTC)
                        );

                        bestellung.setStatus(rs.getString("status"));
                    }

                    produkteForBestellungen(rs, bestellung);
                }

                return Optional.ofNullable(bestellung);
            }, bestellungId);
    }

    public Bestellung createBestellung(Bestellung bestellung) {
        String sql = "INSERT INTO bestellung (datum, status, mitarbeiterzuweis, kunde_id) " +
                "VALUES (?, ?, ?, ?) RETURNING bestellung_id";

        Integer id = jdbcTemplate.queryForObject(
            sql,
            (rs, rowNum) -> rs.getInt("bestellung_id"),
            bestellung.getDatum(),
            bestellung.getStatus(),
            bestellung.getPersonalNr(),
            bestellung.getKundeId()
        );

        if (id != null) {
            bestellung.setBestellungId(id);
        } else {
            throw new RuntimeException("Bestellung id is null");
        }
        return bestellung;
    }

    public boolean deleteById(int bestellungId) {
        String sql = "DELETE FROM bestellung WHERE bestellung_id = ?";
        return jdbcTemplate.update(sql, bestellungId) > 0;
    }

    private void produkteForBestellungen(ResultSet rs, Bestellung bestellung) throws SQLException {
        Produkt produkt = new Produkt();
        produkt.setSku(rs.getString("sku"));
        produkt.setName(rs.getString("name"));
        produkt.setPreis(rs.getBigDecimal("preis"));
        produkt.setLagerbestand(rs.getInt("lagerbestand"));
        produkt.setAngelegtVon(rs.getInt("angelegt_von"));

        PositionenFuerBestellung pos = new PositionenFuerBestellung();
        pos.setPositionsId(rs.getInt("position_id"));
        pos.setBestellungId(rs.getInt("bestellung_id"));
        pos.setMenge(rs.getInt("menge"));
        pos.setGesamtpreis(rs.getBigDecimal("gesamtpreis"));
        pos.setProdukt(produkt);

        bestellung.getPositionen().add(pos);
    }
}

