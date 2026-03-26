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
import simpleshopapi.model.*;
import simpleshopapi.repositories.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestcontainerConfiguration.class, BestellungContainerTest.TestConfig.class})
@Sql("/schema-test.sql")
class BestellungContainerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private BestellungRepository bestellungRepository;
    private KundenRepository kundenRepository;
    private MitarbeiterRepository mitarbeiterRepository;
    private ProduktRepository produktRepository;
    private BestellpositionenRepository bestellpositionenRepository;

    @BeforeEach
    void setup(){
        bestellungRepository = new BestellungRepository(jdbcTemplate);
        kundenRepository = new KundenRepository(jdbcTemplate, passwordEncoder);
        mitarbeiterRepository = new MitarbeiterRepository(jdbcTemplate, passwordEncoder);
        produktRepository = new ProduktRepository(jdbcTemplate);
        bestellpositionenRepository = new BestellpositionenRepository(jdbcTemplate);
    }

    @Test
    void testCreateFindDeleteBestellung() {
        Kunde kunde = new Kunde();
        kunde.setEmail("kunde@test.de");
        kunde.setVorname("Max");
        kunde.setNachname("Mustermann");
        kunde.setPasswort("pass123");
        kundenRepository.save(kunde);

        Mitarbeiter mitarbeiter = new Mitarbeiter();
        mitarbeiter.setEmail("ma@test.de");
        mitarbeiter.setVorname("Anna");
        mitarbeiter.setNachname("Muster");
        mitarbeiter.setPasswort("pass123");
        mitarbeiterRepository.save(mitarbeiter);

        Produkt produkt = new Produkt();
        produkt.setSku("SKU-0000");
        produkt.setName("Produkt1");
        produkt.setPreis(BigDecimal.valueOf(10));
        produkt.setLagerbestand(100);
        produkt.setAngelegtVon(mitarbeiter.getPersonalNr());
        produktRepository.createProdukt(produkt);

        Bestellung bestellung = new Bestellung();
        bestellung.setDatum(Instant.now().atOffset(ZoneOffset.UTC));
        bestellung.setStatus("neu");
        bestellung.setPersonalNr(mitarbeiter.getPersonalNr());
        bestellung.setKundeId(kunde.getKundeId());

        // CREATE
        Bestellung created = bestellungRepository.createBestellung(bestellung);
        assertThat(created.getBestellungId()).isNotNegative();

        Bestellpositionen bp = bestellpositionenRepository.createBestellposition(2, produkt.getSku(), created.getBestellungId());

        // FIND
        Optional<Bestellung> fetched = bestellungRepository.findById(created.getBestellungId());
        assertTrue(fetched.isPresent());
        assertEquals(1, fetched.get().getPositionen().size());
        assertEquals(produkt.getSku(), fetched.get().getPositionen().get(0).getProdukt().getSku());

        // DELETE
        bestellpositionenRepository.deleteById(bp.getPositionsId());
        assertTrue(bestellungRepository.deleteById(created.getBestellungId()));
        assertFalse(bestellungRepository.findById(created.getBestellungId()).isPresent());
    }
}
