package simpleshopapi.repositories;

import simpleshopapi.model.Adresse;
import simpleshopapi.dto.AdresseMitTypDTO;
import simpleshopapi.model.Kunde;
import simpleshopapi.dto.KundeLoginDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class KundenRepository {

    private final JdbcTemplate jdbcTemplate;

    public KundenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Kunde> findAll() {
    String sql =
        """
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
        JOIN kunde_hat_adressen kha ON kha.kunde_id = k.kunde_id
        JOIN adresse a ON a.adresse_id = kha.adresse_id
        ORDER BY k.kunde_id
        """;
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
        String sql = """
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
        JOIN kunde_hat_adressen kha ON kha.kunde_id = k.kunde_id
        JOIN adresse a ON a.adresse_id = kha.adresse_id
        WHERE k.kunde_id = ?
        ORDER BY a.adresse_id
        """;

        return jdbcTemplate.query(sql, (ResultSet rs) -> {
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
        }, id);
    }

    public Optional<Kunde> findByEmail(String email) {
        String sql = """
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
        JOIN kunde_hat_adressen kha ON kha.kunde_id = k.kunde_id
        JOIN adresse a ON a.adresse_id = kha.adresse_id
        WHERE k.email = ?
        ORDER BY a.adresse_id
        """;

        return jdbcTemplate.query(sql, (ResultSet rs) -> {
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
        }, email);
    }

    public Kunde createKunde(Kunde kunde) {
        String sql = "INSERT INTO kunde (email, vorname, nachname, passwort) " +
                "VALUES (?, ?, ?, ?) RETURNING kunde_id";

        Integer id = jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> rs.getInt("kunde_id"),
                kunde.getEmail(),
                kunde.getVorname(),
                kunde.getNachname(),
                kunde.getPasswort()
        );

        if (id != null) {
            kunde.setKundeId(id);
        } else {
            throw new RuntimeException("Kunde id is null");
        }
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

        return jdbcTemplate.update(
            sql,
            kunde.getEmail(),
            kunde.getVorname(),
            kunde.getNachname(),
            kunde.getPasswort(),
            kunde.getKundeId()
        );
    }

    public Kunde login(KundeLoginDTO kundeLogin) {
        String sql = "SELECT * FROM kunde WHERE email = ? AND passwort = ?";

        List<Kunde> kundenListe = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Kunde kunde = new Kunde();
                    kunde.setKundeId(rs.getInt("kunde_id"));
                    kunde.setEmail(rs.getString("email"));
                    kunde.setVorname(rs.getString("vorname"));
                    kunde.setNachname(rs.getString("nachname"));
                    kunde.setPasswort(null);
                    return kunde;
                },
                kundeLogin.getEmail(),
                kundeLogin.getPasswort()
        );

        return kundenListe.isEmpty() ? new Kunde() : kundenListe.get(0);
    }

    private void adressenForKunde(ResultSet rs, Kunde kunde) throws SQLException {
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

}
