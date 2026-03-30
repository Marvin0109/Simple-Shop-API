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
import simpleshopapi.model.Kunde;
import simpleshopapi.model.KundenAdresse;
import simpleshopapi.repositories.KundenAdresseRepository;
import simpleshopapi.repositories.KundenRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestcontainerConfiguration.class, KundenAdresseContainerTest.TestConfig.class})
@Sql({"/schema-test.sql", "/data-test.sql"})
class KundenAdresseContainerTest {

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
    private KundenAdresseRepository repository;

    @BeforeEach
    void setUp() {
        repository = new KundenAdresseRepository(jdbcTemplate);
        kundenRepository = new KundenRepository(jdbcTemplate, passwordEncoder);
    }

    @Test
    void testCrud() {
        KundenAdresse kundenAdresse = new KundenAdresse();
        kundenAdresse.setKundenId(1);
        kundenAdresse.setAdresseId(1);
        kundenAdresse.setTyp("Lieferadresse");

        // CREATE
        repository.save(kundenAdresse);

        Optional<Kunde> loaded = kundenRepository.findById(1);

        assert(loaded).isPresent();
        assertThat(loaded.get().getAdressen()).isNotEmpty();

        // UPDATE
        repository.update("Rechnungsadresse", kundenAdresse);

        Optional<Kunde> loaded2 = kundenRepository.findById(1);

        assert(loaded2).isPresent();
        var adressen = loaded2.get().getAdressen();

        assertThat(adressen.get(0).getTyp()).isEqualTo("Rechnungsadresse");

        // DELETE
        kundenAdresse.setTyp("Rechnungsadresse");
        repository.delete(kundenAdresse);

        Optional<Kunde> deletedAdress = kundenRepository.findById(1);
        assert(deletedAdress).isPresent();

        assertThat(deletedAdress.get().getAdressen()).isEmpty();
    }
}
