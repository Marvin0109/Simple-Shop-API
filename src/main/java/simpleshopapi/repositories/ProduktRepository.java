package simpleshopapi.repositories;

import org.springframework.dao.DuplicateKeyException;
import simpleshopapi.model.Produkt;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProduktRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProduktRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Produkt> findAll() {
        String sql = "SELECT * FROM produkt";
        return jdbcTemplate.query(sql, PRODUKT_ROW_MAPPER);
    }

    public Optional<Produkt> findBySKU(String sku) {
        String sql = "SELECT * FROM produkt WHERE sku = ?";
        return jdbcTemplate.query(sql, PRODUKT_ROW_MAPPER, sku)
                .stream()
                .findFirst();
    }

    public Produkt createProdukt(Produkt produkt) {
        if (findBySKU(produkt.getSku()).isPresent()) {
            throw new IllegalStateException("Produkt exists with sku " + produkt.getSku());
        }

        String sql = "INSERT INTO produkt (sku, name, preis, lagerbestand, angelegt_von) " +
        "VALUES (?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.update(
                    sql,
                    produkt.getSku(),
                    produkt.getName(),
                    produkt.getPreis(),
                    produkt.getLagerbestand(),
                    produkt.getAngelegtVon()
            );
        } catch (DuplicateKeyException e) {
            throw new IllegalStateException("Produkt exists with sku " + produkt.getSku());
        }

        return produkt;
    }

    public int deleteBySKU(String sku) {
        String sql = "DELETE FROM produkt WHERE sku = ?";
        return jdbcTemplate.update(sql, sku);
    }

    public int updateLagerbestand(String sku, int lagerbestand) {
        String sql = "UPDATE produkt SET lagerbestand = ? WHERE sku = ?";
        return jdbcTemplate.update(sql, lagerbestand, sku);
    }

    private static final RowMapper<Produkt> PRODUKT_ROW_MAPPER =
        (rs, rowNum) -> {
            Produkt produkt = new Produkt();
            produkt.setSku(rs.getString("sku"));
            produkt.setName(rs.getString("name"));
            produkt.setPreis(rs.getBigDecimal("preis"));
            produkt.setLagerbestand(rs.getInt("lagerbestand"));
            produkt.setAngelegtVon(rs.getInt("angelegt_von"));
            return produkt;
        };
}
