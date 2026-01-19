package simpleshopapi.repositories;

import simpleshopapi.dto.KundeSummeAnzahlBestellungDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KundeUebersichtRepository {

    private final JdbcTemplate jdbcTemplate;

    public KundeUebersichtRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<KundeSummeAnzahlBestellungDTO> findAll() {
        String sql = "SELECT * FROM v_kunde_summe_anzahl_bestellungen";

        return jdbcTemplate.query(sql, rowMapper());
    }

    private RowMapper<KundeSummeAnzahlBestellungDTO> rowMapper() {
        return (rs, rowNum) -> new KundeSummeAnzahlBestellungDTO(
            rs.getInt("kunde_id"),
            rs.getString("email"),
            rs.getInt("anzahl_bestellungen"),
            rs.getBigDecimal("gesamtsumme")
        );
    }
}
