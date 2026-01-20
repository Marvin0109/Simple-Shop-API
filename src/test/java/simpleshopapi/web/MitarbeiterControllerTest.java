package simpleshopapi.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import simpleshopapi.controller.MitarbeiterController;
import simpleshopapi.exception.MitarbeiterNotFoundException;
import simpleshopapi.model.Mitarbeiter;
import simpleshopapi.service.MitarbeiterService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MitarbeiterController.class)
public class MitarbeiterControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private MitarbeiterService service;

    @Test
    void getMitarbeiter() throws Exception {
        Mitarbeiter m = new Mitarbeiter();
        m.setPersonalNr(1);
        m.setEmail("max@firma.de");
        when(service.findAll()).thenReturn(List.of(m));

        mvc.perform(get("/mitarbeiter"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].personalNr").value(1))
            .andExpect(jsonPath("$.[0].email").value("max@firma.de"));
    }

    @Test
    void getMitarbeiter_withId() throws Exception {
        Mitarbeiter m =  new Mitarbeiter();
        m.setPersonalNr(1);
        m.setVorname("Max");
        when(service.findById(1)).thenReturn(m);

        mvc.perform(get("/mitarbeiter").param("id", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.personalNr").value(1))
            .andExpect(jsonPath("$.vorname").value("Max"));
    }

    @Test
    void getMitarbeiter_withId_notFound() throws Exception {
        when(service.findById(99)).thenThrow(new MitarbeiterNotFoundException(99));

        mvc.perform(get("/mitarbeiter").param("id", "99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void createMitarbeiter_success_returnsCreated() throws Exception {

        Mitarbeiter saved = new Mitarbeiter();
        saved.setPersonalNr(1);
        saved.setVorname("Max");

        when(service.create(any(Mitarbeiter.class))).thenReturn(saved);

        mvc.perform(post("/mitarbeiter")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                  {
                    "passwort": "123#a",
                    "email": "max@test.db",
                    "vorname": "Max",
                    "nachname": "Mustermann"
                  }
                """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.personalNr").value(1))
            .andExpect(jsonPath("$.vorname").value("Max"));
    }

    @Test
    void createMitarbeiter_fail_returnsCreated() throws Exception {

        mvc.perform(post("/mitarbeiter")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                  {
                    "passwort": "password",
                    "email": "max@test.db",
                    "vorname": "Max",
                    "nachname": "Mustermann"
                  }
                """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createMitarbeiter_fail2_returnsCreated() throws Exception {

        mvc.perform(post("/mitarbeiter")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                  {
                    "email": "max.test@.db",
                    "vorname": "Max",
                    "nachname": "Mustermann"
                  }
                """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void deleteMitarbeiter_existing_returnsNoContent() throws Exception {
        doNothing().when(service).delete(1);

        mvc.perform(delete("/mitarbeiter")
                .param("id", "1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteMitarbeiter_notFound_returns404() throws Exception {
        doThrow(new MitarbeiterNotFoundException(99)).when(service).delete(99);

        mvc.perform(delete("/mitarbeiter")
                .param("id", "99"))
            .andExpect(status().isNotFound());
    }

}
