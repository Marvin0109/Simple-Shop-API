package simpleshopapi.application;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import simpleshopapi.repository.TestcontainerConfiguration;

@SpringBootTest
@Import(TestcontainerConfiguration.class)
class ApplicationTests {

    @Test
    void contextLoads() {

    }
}
