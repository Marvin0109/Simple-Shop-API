package simpleshopapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import simpleshopapi.model.Produkt;
import simpleshopapi.repositories.ProduktRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainerConfiguration.class)
@Sql("/schema-test.sql")
public class ProduktContainerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ProduktRepository repository;
    private Integer mitarbeiterId;

    @BeforeEach
    void setUp() {
        repository = new ProduktRepository(jdbcTemplate);

        jdbcTemplate.update("INSERT INTO mitarbeiter (vorname, nachname, email, passwort) " +
                "VALUES ('Max','Mustermann','max@test.de','pw123!')");
        mitarbeiterId = jdbcTemplate.queryForObject("SELECT personal_nr FROM mitarbeiter LIMIT 1",
                Integer.class);
    }

    @Test
    void testCreateFindUpdateDeleteProdukt() {
        Produkt p = new Produkt();
        p.setSku("SKU-0000");
        p.setName("Testprodukt");
        p.setPreis(BigDecimal.valueOf(9.99));
        p.setLagerbestand(10);
        p.setAngelegtVon(mitarbeiterId);

        // CREATE
        repository.createProdukt(p);

        // FIND
        Optional<Produkt> gefunden = repository.findBySKU("SKU-0000");
        assertTrue(gefunden.isPresent());
        assertEquals("Testprodukt", gefunden.get().getName());

        // UPDATE
        repository.updateLagerbestand("SKU-0000", 20);
        assertEquals(20, repository.findBySKU("SKU-0000").get().getLagerbestand());

        // DELETE
        assertTrue(repository.deleteBySKU("SKU-0000"));
        assertFalse(repository.findBySKU("SKU-0000").isPresent());
    }
}
