package simpleshopapi.application;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import simpleshopapi.repository.TestcontainerConfiguration;

@SpringBootTest
@Import(TestcontainerConfiguration.class)
public class ApplicationTests {

    @Test
    void contextLoads() {

    }
}
