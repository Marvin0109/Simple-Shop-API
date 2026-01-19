package simpleshopapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import simpleshopapi.model.Bestellpositionen;
import simpleshopapi.repositories.BestellpositionenRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainerConfiguration.class)
@Sql(scripts = "/schema-test.sql")
public class BestellpositionenContainerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private BestellpositionenRepository repository;

    private String sku;
    private Integer bestellungId;

    @BeforeEach
    void setup() {
        repository = new BestellpositionenRepository(jdbcTemplate);

        jdbcTemplate.update("INSERT INTO mitarbeiter (vorname, nachname, email, passwort) " +
                                "VALUES ('Max','Mustermann','max@test.de','pw123!')");
        Integer mitarbeiterId = jdbcTemplate.queryForObject("SELECT personal_nr FROM mitarbeiter LIMIT 1",
                                                            Integer.class);

        jdbcTemplate.update("INSERT INTO produkt (sku, name, preis, lagerbestand, angelegt_von) " +
                                "VALUES (?, ?, ?, ?, ?)",
                "SKU123", "Testprodukt", BigDecimal.valueOf(10.50), 100, mitarbeiterId);

        sku = jdbcTemplate.queryForObject("SELECT sku FROM produkt LIMIT 1", String.class);

        jdbcTemplate.update("INSERT INTO kunde (email, vorname, nachname, passwort) " +
                                "VALUES ('kunde@test.de','Max','Kunde','pw123!')");
        Integer kundeId = jdbcTemplate.queryForObject("SELECT kunde_id FROM kunde LIMIT 1", Integer.class);

        jdbcTemplate.update("INSERT INTO mitarbeiter (vorname, nachname, email, passwort) " +
                                "VALUES ('Anna','Mitarbeiter','anna@test.de','pw123!')");

        Integer mitarbeiter2Id = jdbcTemplate.queryForObject("SELECT personal_nr FROM mitarbeiter LIMIT 1 OFFSET 1",
                                                                Integer.class);

        jdbcTemplate.update("INSERT INTO bestellung (datum, status, mitarbeiterzuweis, kunde_id) " +
                                "VALUES (NOW(), 'neu', ?, ?)",
                mitarbeiter2Id, kundeId);

        bestellungId = jdbcTemplate.queryForObject("SELECT bestellung_id FROM bestellung LIMIT 1", Integer.class);
    }

    @Test
    void createFindDeleteBestellposition() {
        // CREATE
        Bestellpositionen bp = repository.createBestellposition(5, sku, bestellungId);
        assertThat(bp.getMenge()).isEqualTo(5);
        assertThat(bp.getProduktSku()).isEqualTo(sku);
        assertThat(bp.getGesamtpreis()).isEqualByComparingTo(BigDecimal.valueOf(52.50));

        // FIND
        Optional<Bestellpositionen> found = repository.findById(bp.getPositionsId());
        assertThat(found).isPresent();
        assertThat(found.get().getMenge()).isEqualTo(5);

        List<Bestellpositionen> all = repository.findAll();
        assertThat(all).extracting("positionsId").contains(bp.getPositionsId());

        // DELETE
        boolean deleted = repository.deleteById(bp.getPositionsId());
        assertThat(deleted).isTrue();

        assertThat(repository.findById(bp.getPositionsId())).isEmpty();
    }
}
