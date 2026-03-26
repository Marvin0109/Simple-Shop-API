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
import simpleshopapi.model.Mitarbeiter;
import simpleshopapi.repositories.MitarbeiterRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestcontainerConfiguration.class, MitarbeiterContainerTest.TestConfig.class})
@Sql(scripts = "/schema-test.sql")
class MitarbeiterContainerTest {

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

    private MitarbeiterRepository repository;

    @BeforeEach
    void setup() {
        repository = new MitarbeiterRepository(jdbcTemplate, passwordEncoder);
    }

    @Test
    void createFindDeleteMitarbeiter() {
        Mitarbeiter m = new Mitarbeiter();
        m.setPasswort("geheim");
        m.setEmail("max@mustermann.de");
        m.setVorname("Max");
        m.setNachname("Mustermann");

        // CREATE
        Mitarbeiter saved = repository.save(m);
        assertThat(saved.getPersonalNr()).isNotNull();

        // FIND
        Mitarbeiter found = repository.findById(saved.getPersonalNr()).orElseThrow();
        assertThat(found.getEmail()).isEqualTo("max@mustermann.de");

        // DELETE
        int deleted = repository.deleteById(saved.getPersonalNr());
        assertThat(deleted).isOne();

        assertThat(repository.findById(saved.getPersonalNr())).isEmpty();
    }
}
