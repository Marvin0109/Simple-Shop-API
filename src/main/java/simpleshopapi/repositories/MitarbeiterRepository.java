package simpleshopapi.repositories;

import org.springframework.security.crypto.password.PasswordEncoder;
import simpleshopapi.model.Mitarbeiter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class MitarbeiterRepository {

    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    public MitarbeiterRepository(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Mitarbeiter> findAll() {
        String sql = "SELECT * FROM mitarbeiter";
        return jdbcTemplate.query(sql, MITARBEITER_ROW_MAPPER);
    }

    public Optional<Mitarbeiter> findById(Integer id) {
        String sql = "SELECT * FROM mitarbeiter WHERE personal_nr = ?";
        return jdbcTemplate.query(sql, MITARBEITER_ROW_MAPPER, id)
                .stream()
                .findFirst();
    }

    public Mitarbeiter save(Mitarbeiter mitarbeiter) {
        String hashedPassword = passwordEncoder.encode(mitarbeiter.getPasswort());

        String sql =
                "INSERT INTO mitarbeiter (passwort, email, vorname, nachname) " +
                "VALUES (?, ?, ?, ?) RETURNING personal_nr";

        Integer id = jdbcTemplate.queryForObject(
            sql,
            (rs,rowNum) -> rs.getInt("personal_nr"),
            hashedPassword,
            mitarbeiter.getEmail(),
            mitarbeiter.getVorname(),
            mitarbeiter.getNachname()
        );

        mitarbeiter.setPersonalNr(Objects.requireNonNull(id));
        return mitarbeiter;
    }

    public int deleteById(int id) {
        String sql = "DELETE FROM mitarbeiter WHERE personal_nr = ?";
        return jdbcTemplate.update(sql, id);
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
