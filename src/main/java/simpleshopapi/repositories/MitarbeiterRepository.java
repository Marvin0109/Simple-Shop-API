package simpleshopapi.repositories;

import simpleshopapi.model.Mitarbeiter;
import simpleshopapi.dto.MitarbeiterLoginDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MitarbeiterRepository {

    private final JdbcTemplate jdbcTemplate;

    public MitarbeiterRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mitarbeiter> findAll() {
        String sql = "SELECT * FROM mitarbeiter";
        return jdbcTemplate.query(sql, MITARBEITER_ROW_MAPPER);
    }

    public Optional<Mitarbeiter> findById(Integer id) {
        String sql = "SELECT * FROM mitarbeiter WHERE personal_nr = ?";
        List<Mitarbeiter> result = jdbcTemplate.query(sql, MITARBEITER_ROW_MAPPER, id);
        return result.stream().findFirst();
    }

    public Mitarbeiter createMitarbeiter(Mitarbeiter mitarbeiter) {
        String sql =
                "INSERT INTO mitarbeiter (passwort, email, vorname, nachname) " +
                "VALUES (?, ?, ?, ?) RETURNING personal_nr";

        Integer id = jdbcTemplate.queryForObject(
            sql,
            (rs,rowNum) -> rs.getInt("personal_nr"),
            mitarbeiter.getPasswort(),
            mitarbeiter.getEmail(),
            mitarbeiter.getVorname(),
            mitarbeiter.getNachname()
        );

        if (id != null) {
            mitarbeiter.setPersonalNr(id);
        } else {
            throw new RuntimeException("Mitarbeiter id is null");
        }
        return mitarbeiter;
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM mitarbeiter WHERE personal_nr = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    public Mitarbeiter login(MitarbeiterLoginDTO mitarbeiterLogin) {
        String sql = "SELECT * FROM mitarbeiter WHERE personal_nr = ? AND passwort = ?";

        List<Mitarbeiter> mitarbeiterListe = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Mitarbeiter m = new Mitarbeiter();
                    m.setPersonalNr(rs.getInt("personal_nr"));
                    m.setPasswort(null);
                    m.setEmail(rs.getString("email"));
                    m.setVorname(rs.getString("vorname"));
                    m.setNachname(rs.getString("nachname"));
                    return m;
                },
                mitarbeiterLogin.getPersonalNr(),
                mitarbeiterLogin.getPasswort()
        );

        return mitarbeiterListe.isEmpty() ? new Mitarbeiter() : mitarbeiterListe.get(0);
    }

    private static final RowMapper<Mitarbeiter> MITARBEITER_ROW_MAPPER =
        (rs, rowNum) -> {
            Mitarbeiter m = new Mitarbeiter();
            m.setPersonalNr(rs.getInt("personal_nr"));
            m.setPasswort(rs.getString("passwort"));
            m.setEmail(rs.getString("email"));
            m.setVorname(rs.getString("vorname"));
            m.setNachname(rs.getString("nachname"));
            return m;
        };
}
