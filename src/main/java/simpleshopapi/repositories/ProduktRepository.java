package simpleshopapi.repositories;

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
        List<Produkt> result = jdbcTemplate.query(sql, PRODUKT_ROW_MAPPER, sku);
        return result.stream().findFirst();
    }

    public Produkt createProdukt(Produkt produkt) {
        String sql = "INSERT INTO produkt (sku, name, preis, lagerbestand, angelegt_von) " +
        "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                produkt.getSku(),
                produkt.getName(),
                produkt.getPreis(),
                produkt.getLagerbestand(),
                produkt.getAngelegtVon()
        );

        return produkt;
    }

    public boolean deleteBySKU(String sku) {
        String sql = "DELETE FROM produkt WHERE sku = ?";
        int rowsAffected = jdbcTemplate.update(sql, sku);
        return rowsAffected > 0;
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
