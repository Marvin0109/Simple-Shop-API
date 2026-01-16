package simpleshopapi.repositories;

import simpleshopapi.model.Adresse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AdresseRepository {

    private final JdbcTemplate jdbcTemplate;

    public AdresseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Adresse> findAll() {
        String sql = "SELECT * FROM adresse";
        return jdbcTemplate.query(sql, ADRESSE_ROW_MAPPER);
    }

    public Optional<Adresse> findById(int id) {
        String sql = "SELECT * FROM adresse WHERE adresse_id = ?";
        List<Adresse> result = jdbcTemplate.query(sql, ADRESSE_ROW_MAPPER, id);
        return result.stream().findFirst();
    }

    public Adresse createAdresse(Adresse adresse) {
        String sql = "INSERT INTO adresse (aktiv, strasse, hausnummer, plz, ort, land) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING adresse_id";

        Integer id = jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> rs.getInt("adresse_id"),
                adresse.isAktiv(),
                adresse.getStrasse(),
                adresse.getHausnummer(),
                adresse.getPlz(),
                adresse.getOrt(),
                adresse.getLand()
        );

        if (id != null) {
            adresse.setAdresseId(id);
        } else {
            throw new RuntimeException("Adresse id is null");
        }
        return adresse;
    }

    public int updateAdresse(Adresse adresse) {
        String sql = """
        UPDATE adresse
        SET aktiv = ?,
            strasse = ?,
            hausnummer = ?,
            plz = ?,
            ort = ?,
            land = ?
        WHERE adresse_id = ?
        """;

        return jdbcTemplate.update(
            sql,
            adresse.isAktiv(),
            adresse.getStrasse(),
            adresse.getHausnummer(),
            adresse.getPlz(),
            adresse.getOrt(),
            adresse.getLand(),
            adresse.getAdresseId()
        );
    }

    private static final RowMapper<Adresse> ADRESSE_ROW_MAPPER =
        (rs, rowNum) -> {
            Adresse adresse = new Adresse();
            adresse.setAdresseId(rs.getInt("adresse_id"));
            adresse.setAktiv(rs.getBoolean("aktiv"));
            adresse.setStrasse(rs.getString("strasse"));
            adresse.setHausnummer(rs.getString("hausnummer"));
            adresse.setPlz(rs.getString("plz"));
            adresse.setOrt(rs.getString("ort"));
            adresse.setLand(rs.getString("land"));
            return adresse;
        };
}
