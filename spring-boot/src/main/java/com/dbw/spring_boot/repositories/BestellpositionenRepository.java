package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Bestellpositionen;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BestellpositionenRepository {

    private final JdbcTemplate jdbcTemplate;

    public BestellpositionenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Bestellpositionen> findAll() {
        String sql =
            """
            SELECT
                position_id,
                menge,
                bp.sku,
                bestellung_id,
                p.preis * menge AS gesamtpreis
            FROM bestellposition bp
            JOIN produkt p ON p.sku = bp.sku
            ORDER BY position_id
        """;

        return jdbcTemplate.query(sql, BESTELLPOSITIONEN_ROW_MAPPER);
    }

    public Optional<Bestellpositionen> findById(int id) {
        String sql =
            """
            SELECT
                position_id,
                menge,
                bp.sku,
                bestellung_id,
                p.preis * menge AS gesamtpreis
            FROM bestellposition bp
            JOIN produkt p ON p.sku = bp.sku
            WHERE position_id = ?
        """;

        List<Bestellpositionen> result =  jdbcTemplate.query(sql, BESTELLPOSITIONEN_ROW_MAPPER, id);
        return result.stream().findFirst();
    }

    public Bestellpositionen createBestellposition(int menge, String sku, int bestellungId) {
        String sql =
                "INSERT INTO bestellposition (menge, sku, bestellung_id) " +
                "VALUES (?, ?, ?) RETURNING position_id, menge, sku, bestellung_id, menge * " +
                "(SELECT preis FROM produkt WHERE sku = ?) AS gesamtpreis";

        return jdbcTemplate.queryForObject(sql, BESTELLPOSITIONEN_ROW_MAPPER, menge, sku, bestellungId, sku);
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM bestellposition WHERE position_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    private static final RowMapper<Bestellpositionen> BESTELLPOSITIONEN_ROW_MAPPER =
        (rs, rowNum) -> {
            Bestellpositionen bp = new Bestellpositionen();
            bp.setPositionsId(rs.getInt("position_id"));
            bp.setMenge(rs.getInt("menge"));
            bp.setProduktSku(rs.getString("sku"));
            bp.setBestellungId(rs.getInt("bestellung_id"));
            bp.setGesamtpreis(rs.getBigDecimal("gesamtpreis"));
            return bp;
        };
}
