package simpleshopapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import simpleshopapi.dto.AdresseMitTypDTO;
import simpleshopapi.model.Adresse;
import simpleshopapi.model.Kunde;
import simpleshopapi.repositories.AdresseRepository;
import simpleshopapi.repositories.KundenRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestcontainerConfiguration.class, KundeContainerTest.TestConfig.class})
@Sql(scripts = "/schema-test.sql")
class KundeContainerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private KundenRepository kundenRepository;
    private AdresseRepository adresseRepository;

    @BeforeEach
    void setup() {
        kundenRepository = new KundenRepository(jdbcTemplate, passwordEncoder);
        adresseRepository = new AdresseRepository(jdbcTemplate);
    }

    @Test
    void createFindUpdateKundeWithAdresse() {
        Adresse adresse = new Adresse();
        adresse.setAktiv(true);
        adresse.setStrasse("Hauptstrasse");
        adresse.setHausnummer("1");
        adresse.setPlz("12345");
        adresse.setOrt("Musterstadt");
        adresse.setLand("Deutschland");

        Adresse savedAdresse = adresseRepository.save(adresse);

        Kunde kunde = new Kunde();
        kunde.setEmail("max@mustermann.de");
        kunde.setVorname("Max");
        kunde.setNachname("Mustermann");
        kunde.setPasswort("geheim");

        // CREATE
        Kunde savedKunde = kundenRepository.save(kunde);

        jdbcTemplate.update(
                "INSERT INTO kunde_hat_adressen (kunde_id, adresse_id, typ) VALUES (?, ?, ?)",
                savedKunde.getKundeId(),
                savedAdresse.getAdresseId(),
                "Lieferadresse"
        );

        // FIND
        Kunde found = kundenRepository.findById(savedKunde.getKundeId()).orElseThrow();
        assertThat(found.getEmail()).isEqualTo("max@mustermann.de");
        assertThat(found.getAdressen()).hasSize(1);
        AdresseMitTypDTO amt = found.getAdressen().get(0);
        assertThat(amt.getAdresse().getStrasse()).isEqualTo("Hauptstrasse");
        assertThat(amt.getTyp()).isEqualTo("Lieferadresse");

        Kunde foundByEmail = kundenRepository.findByEmail("max@mustermann.de").orElseThrow();
        assertThat(foundByEmail.getKundeId()).isEqualTo(savedKunde.getKundeId());

        // UPDATE
        found.setVorname("Moritz");
        int updatedRows = kundenRepository.updateKunde(found);
        assertThat(updatedRows).isEqualTo(1);

        Kunde updated = kundenRepository.findById(savedKunde.getKundeId()).orElseThrow();
        assertThat(updated.getVorname()).isEqualTo("Moritz");

        List<Kunde> allKunden = kundenRepository.findAll();
        assertThat(allKunden).extracting("kundeId").contains(savedKunde.getKundeId());
    }

}
