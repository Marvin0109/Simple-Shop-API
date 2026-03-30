package simpleshopapi.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import simpleshopapi.model.KundenAdresse;

@Repository
public class KundenAdresseRepository {

    private final JdbcTemplate jdbcTemplate;

    public KundenAdresseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public KundenAdresse save(KundenAdresse kundenAdresse) {
        String sql = "INSERT INTO kunde_hat_adressen (adresse_id, kunde_id, typ) " +
                "VALUES (?, ?, ?) ON CONFLICT DO NOTHING";
        jdbcTemplate.update(
                sql,
                kundenAdresse.getAdresseId(),
                kundenAdresse.getKundenId(),
                kundenAdresse.getTyp()
        );

        return kundenAdresse;
    }

    public int update(String type, KundenAdresse kundenAdresse) {
        String sql = """
            UPDATE kunde_hat_adressen
            SET typ = ?
            WHERE adresse_id = ? AND kunde_id = ? AND typ = ?
        """;

        return jdbcTemplate.update(sql, type,
                kundenAdresse.getAdresseId(),
                kundenAdresse.getKundenId(),
                kundenAdresse.getTyp());
    }

    public int delete(KundenAdresse kundenAdresse) {
        String sql = """
            DELETE FROM kunde_hat_adressen
            WHERE adresse_id = ? AND kunde_id = ? AND typ = ?
        """;

        return jdbcTemplate.update(
                sql,
                kundenAdresse.getAdresseId(),
                kundenAdresse.getKundenId(),
                kundenAdresse.getTyp());
    }
}
