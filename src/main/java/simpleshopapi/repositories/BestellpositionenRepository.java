package simpleshopapi.repositories;

import simpleshopapi.model.Bestellpositionen;
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
        return jdbcTemplate.query(BASE_SELECT + " ORDER BY position_id", BESTELLPOSITIONEN_ROW_MAPPER);
    }

    public Optional<Bestellpositionen> findById(int id) {
        return jdbcTemplate.query(BASE_SELECT + " WHERE position_id = ?", BESTELLPOSITIONEN_ROW_MAPPER, id)
                .stream()
                .findFirst();
    }

    public Bestellpositionen createBestellposition(int menge, String sku, int bestellungId) {
        String sql =
                "INSERT INTO bestellposition (menge, sku, bestellung_id) " +
                "VALUES (?, ?, ?) RETURNING position_id, menge, sku, bestellung_id, menge * " +
                "(SELECT preis FROM produkt WHERE sku = ?) AS gesamtpreis";

        return jdbcTemplate.queryForObject(sql, BESTELLPOSITIONEN_ROW_MAPPER, menge, sku, bestellungId, sku);
    }

    public int deleteById(int id) {
        String sql = "DELETE FROM bestellposition WHERE position_id = ?";
        return jdbcTemplate.update(sql, id);
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

    private static final String BASE_SELECT = """
            SELECT
                position_id,
                menge,
                bp.sku,
                bestellung_id,
                p.preis * menge AS gesamtpreis
            FROM bestellposition bp
            JOIN produkt p ON p.sku = bp.sku
        """;
}
