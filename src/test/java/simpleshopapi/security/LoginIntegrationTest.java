package simpleshopapi.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import simpleshopapi.repository.TestcontainerConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import({TestcontainerConfiguration.class})
class LoginIntegrationTest {

    @Autowired
    PasswordEncoder encoder;

    @Test
    void bcryptWorks() {
        String hash = encoder.encode("password");
        assertThat(encoder.matches("password", hash)).isTrue();
    }
}
