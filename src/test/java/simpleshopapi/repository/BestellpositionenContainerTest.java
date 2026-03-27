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
@Sql(scripts = {"/schema-test.sql", "/data-test.sql"})
class BestellpositionenContainerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private BestellpositionenRepository repository;

    @BeforeEach
    void setup() {
        repository = new BestellpositionenRepository(jdbcTemplate);
    }

    @Test
    void createFindDeleteBestellposition() {
        // CREATE
        Bestellpositionen bp = repository.createBestellposition(5, "SKU123", 1);
        assertThat(bp.getMenge()).isEqualTo(5);
        assertThat(bp.getProduktSku()).isEqualTo("SKU123");
        assertThat(bp.getGesamtpreis()).isEqualByComparingTo(BigDecimal.valueOf(52.50));

        // FIND
        Optional<Bestellpositionen> found = repository.findById(bp.getPositionsId());
        assertThat(found).isPresent();
        assertThat(found.get().getMenge()).isEqualTo(5);

        List<Bestellpositionen> all = repository.findAll();
        assertThat(all).extracting("positionsId").contains(bp.getPositionsId());

        // DELETE
        int deleted = repository.deleteById(bp.getPositionsId());
        assertThat(deleted).isOne();

        assertThat(repository.findById(bp.getPositionsId())).isEmpty();
    }
}
