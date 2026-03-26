package simpleshopapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import simpleshopapi.dto.KundeLoginDTO;
import simpleshopapi.dto.LoadKundeDTO;
import simpleshopapi.exception.UnauthorizedException;
import simpleshopapi.model.Kunde;
import simpleshopapi.repositories.KundenRepository;
import simpleshopapi.repositories.MitarbeiterRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class LoginServiceTest {

    private LoginService service;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private MitarbeiterRepository mitarbeiterRepository;

    @Mock
    private KundenRepository kundenRepository;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        service = new LoginService(mitarbeiterRepository, kundenRepository, encoder);
    }

    @Test
    void loginKunde() {
        KundeLoginDTO k = new KundeLoginDTO();
        k.setEmail("email");
        k.setPasswort("passwort");

        Kunde loaded = new Kunde();
        loaded.setEmail("email");
        loaded.setPasswort("hashed");

        when(kundenRepository.findByEmail("email")).thenReturn(Optional.of(loaded));
        when(encoder.matches("passwort", "hashed")).thenReturn(true);

        LoadKundeDTO loggedIn = service.loginKunde(k);

        assertThat(loggedIn.email()).isEqualTo("email");
    }

    @Test
    void loginKunde_wrongPassword() {
        KundeLoginDTO k = new KundeLoginDTO();
        k.setEmail("email");
        k.setPasswort("wrong");

        Kunde loaded = new Kunde();
        loaded.setEmail("email");
        loaded.setPasswort("hashed");

        when(kundenRepository.findByEmail("email"))
                .thenReturn(Optional.of(loaded));

        when(encoder.matches("wrong", "hashed"))
                .thenReturn(false);

        assertThatThrownBy(() -> service.loginKunde(k))
                .isInstanceOf(UnauthorizedException.class);
    }
}
