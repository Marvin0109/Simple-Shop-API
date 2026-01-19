package simpleshopapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import simpleshopapi.model.Adresse;
import simpleshopapi.repositories.AdresseRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainerConfiguration.class)
@Sql(scripts = "/schema-test.sql")
public class AdresseContainerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private AdresseRepository repository;

    @BeforeEach
    void setup() {
        repository = new AdresseRepository(jdbcTemplate);
    }

    @Test
    void createFindUpdateAdresse() {
        Adresse adresse = new Adresse();
        adresse.setAktiv(true);
        adresse.setStrasse("Hauptstrasse");
        adresse.setHausnummer("12a");
        adresse.setPlz("12345");
        adresse.setOrt("Musterstadt");
        adresse.setLand("Deutschland");

        // CREATE
        Adresse saved = repository.createAdresse(adresse);
        assertThat(saved.getAdresseId()).isNotNull();

        // FIND
        Adresse found = repository.findById(saved.getAdresseId()).orElseThrow();
        assertThat(found.getStrasse()).isEqualTo("Hauptstrasse");
        assertThat(found.isAktiv()).isTrue();

        // UPDATE
        saved.setOrt("Neue Stadt");
        saved.setAktiv(false);
        int updatedRows = repository.updateAdresse(saved);
        assertThat(updatedRows).isEqualTo(1);

        Adresse updated = repository.findById(saved.getAdresseId()).orElseThrow();
        assertThat(updated.getOrt()).isEqualTo("Neue Stadt");
        assertThat(updated.isAktiv()).isFalse();

        List<Adresse> all = repository.findAll();
        assertThat(all).isNotEmpty();
        assertThat(all).extracting("adresseId").contains(saved.getAdresseId());
    }
}
