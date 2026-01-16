package simpleshopapi.repositories;

import simpleshopapi.model.ProduktVerkaufszahlenDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProduktUebersichtRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProduktUebersichtRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProduktVerkaufszahlenDTO> findAll() {
        String sql = "SELECT * FROM v_produkt_verkaufszahlen";

        return jdbcTemplate.query(sql, rowMapper());
    }

    private RowMapper<ProduktVerkaufszahlenDTO> rowMapper() {
        return (rs, rowNum) -> new ProduktVerkaufszahlenDTO(
            rs.getString("sku"),
            rs.getString("name"),
            rs.getInt("gesamt_verkaufte_menge"),
            rs.getBigDecimal("umsatz"),
            rs.getInt("anzahl_bestellungen")
        );
    }
}
