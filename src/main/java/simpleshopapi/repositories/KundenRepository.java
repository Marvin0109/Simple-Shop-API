package simpleshopapi.repositories;

import org.springframework.security.crypto.password.PasswordEncoder;
import simpleshopapi.model.Adresse;
import simpleshopapi.dto.AdresseMitTypDTO;
import simpleshopapi.model.Kunde;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class KundenRepository {

    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    public KundenRepository(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Kunde> findAll() {
        String sql = BASE_SELECT + " ORDER BY k.kunde_id";

        return jdbcTemplate.query(sql, (ResultSetExtractor<List<Kunde>>) rs -> {

            Map<Integer, Kunde> kundeMap = new LinkedHashMap<>();

            while (rs.next()) {

                int kundeId = rs.getInt("kunde_id");

                Kunde kunde = kundeMap.get(kundeId);
                if (kunde == null) {
                    kunde = new Kunde();
                    kunde.setKundeId(kundeId);
                    kunde.setEmail(rs.getString("email"));
                    kunde.setVorname(rs.getString("vorname"));
                    kunde.setNachname(rs.getString("nachname"));
                    kunde.setPasswort(rs.getString("passwort"));

                    kundeMap.put(kundeId, kunde);
                }

                adressenForKunde(rs, kunde);
            }

            return new ArrayList<>(kundeMap.values());
        });
    }

    public Optional<Kunde> findById(Integer id) {
        return querySingleKunde(BASE_SELECT + " WHERE k.kunde_id = ? ORDER BY a.adresse_id", id);
    }

    public Optional<Kunde> findByEmail(String email) {
        return querySingleKunde(BASE_SELECT + " WHERE k.email = ? ORDER BY a.adresse_id", email);
    }

    public Kunde save(Kunde kunde) {
        String hashedPassword = passwordEncoder.encode(kunde.getPasswort());

        String sql = "INSERT INTO kunde (email, vorname, nachname, passwort) " +
                "VALUES (?, ?, ?, ?) RETURNING kunde_id";

        Integer id = jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> rs.getInt("kunde_id"),
                kunde.getEmail(),
                kunde.getVorname(),
                kunde.getNachname(),
                hashedPassword
        );

        kunde.setKundeId(Objects.requireNonNull(id));
        kunde.setPasswort(null);
        return kunde;
    }

    public Integer updateKunde(Kunde kunde) {
        String sql = """
        UPDATE kunde
        SET email = ?,
            vorname = ?,
            nachname = ?,
            passwort = ?
        WHERE kunde_id = ?
        """;

        String hashed = passwordEncoder.encode(kunde.getPasswort());

        return jdbcTemplate.update(
            sql,
            kunde.getEmail(),
            kunde.getVorname(),
            kunde.getNachname(),
            hashed,
            kunde.getKundeId()
        );
    }

    private void adressenForKunde(ResultSet rs, Kunde kunde) throws SQLException {
        Integer adresseId = rs.getObject("adresse_id", Integer.class);

        if (adresseId == null) {
            return;
        }

        Adresse adresse = new Adresse();
        adresse.setAdresseId(rs.getInt("adresse_id"));
        adresse.setAktiv(rs.getBoolean("aktiv"));
        adresse.setStrasse(rs.getString("strasse"));
        adresse.setHausnummer(rs.getString("hausnummer"));
        adresse.setPlz(rs.getString("plz"));
        adresse.setOrt(rs.getString("ort"));
        adresse.setLand(rs.getString("land"));

        AdresseMitTypDTO amt = new AdresseMitTypDTO();
        amt.setAdresse(adresse);
        amt.setTyp(rs.getString("typ"));

        kunde.getAdressen().add(amt);
    }

    private Optional<Kunde> querySingleKunde(String sql, Object param) {
        return jdbcTemplate.query(sql, rs -> {
            Kunde kunde = null;

            while (rs.next()) {
                if (kunde == null) {
                    kunde = new Kunde();
                    kunde.setKundeId(rs.getInt("kunde_id"));
                    kunde.setEmail(rs.getString("email"));
                    kunde.setVorname(rs.getString("vorname"));
                    kunde.setNachname(rs.getString("nachname"));
                    kunde.setPasswort(rs.getString("passwort"));
                }

                adressenForKunde(rs, kunde);
            }

            return Optional.ofNullable(kunde);
        }, param);
    }

    private static final String BASE_SELECT = """
        SELECT
            k.kunde_id,
            k.email,
            k.vorname,
            k.nachname,
            k.passwort,
            a.adresse_id,
            a.aktiv,
            a.strasse,
            a.hausnummer,
            a.plz,
            a.ort,
            a.land,
            kha.typ
        FROM kunde k
        LEFT JOIN kunde_hat_adressen kha ON kha.kunde_id = k.kunde_id
        LEFT JOIN adresse a ON a.adresse_id = kha.adresse_id
        """;
}
