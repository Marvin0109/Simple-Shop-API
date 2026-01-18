package simpleshopapi.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import simpleshopapi.controller.LoginController;
import simpleshopapi.exception.UnauthorizedException;
import simpleshopapi.model.Kunde;
import simpleshopapi.model.KundeLogin;
import simpleshopapi.model.Mitarbeiter;
import simpleshopapi.model.MitarbeiterLogin;
import simpleshopapi.service.LoginService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private LoginService service;

    @Test
    void loginMitarbeiter_success_returns200() throws Exception {
        Mitarbeiter m = new Mitarbeiter();
        m.setPersonalNr(1);
        m.setVorname("Max");

        when(service.loginMitarbeiter(any(MitarbeiterLogin.class))).thenReturn(m);

        mvc.perform(post("/login/mitarbeiter")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                  {
                      "personalNr": 1,
                      "passwort": "correct"
                  }
                """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.personalNr").value(1))
            .andExpect(jsonPath("$.vorname").value("Max"));
    }

    @Test
    void loginMitarbeiter_invalid_returns401() throws Exception {
        when(service.loginMitarbeiter(any(MitarbeiterLogin.class)))
                .thenThrow(new UnauthorizedException("Ungültige Login-Daten"));

        mvc.perform(post("/login/mitarbeiter")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                   {
                       "personalNr": 1,
                       "passwort": "wrong"
                   }
                """))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void loginKunde_success_returns200() throws Exception {
        Kunde k = new Kunde();
        k.setKundeId(1);
        k.setVorname("Anna");

        when(service.loginKunde(any(KundeLogin.class))).thenReturn(k);

        mvc.perform(post("/login/kunde")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "email": "anna@test.de",
                        "passwort": "correct"
                    }
                """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.kundeId").value(1))
            .andExpect(jsonPath("$.vorname").value("Anna"));
    }

    @Test
    void loginKunde_invalid_returns401() throws Exception {
        when(service.loginKunde(any(KundeLogin.class)))
            .thenThrow(new UnauthorizedException("Ungültige Login-Daten"));

        mvc.perform(post("/login/kunde")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "email": "wrong@test.de",
                        "passwort": "wrong"
                    }
                """))
            .andExpect(status().isUnauthorized());
    }

}
