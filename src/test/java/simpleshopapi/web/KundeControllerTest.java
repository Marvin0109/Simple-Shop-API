package simpleshopapi.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import simpleshopapi.controller.KundenController;
import simpleshopapi.exception.KundeNotFoundException;
import simpleshopapi.model.Kunde;
import simpleshopapi.service.KundenService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KundenController.class)
public class KundeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private KundenService service;

    @Test
    void getKunden() throws Exception {
        Kunde k = new Kunde();
        k.setKundeId(1);
        k.setVorname("Max");

        when(service.findAll()).thenReturn(List.of(k));

        mvc.perform(get("/kunden"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].kundeId").value(1))
            .andExpect(jsonPath("$[0].vorname").value("Max"));
    }

    @Test
    void getKunden_withId() throws Exception {
        Kunde k = new Kunde();
        k.setKundeId(1);
        k.setVorname("Max");

        when(service.findById(1)).thenReturn(k);

        mvc.perform(get("/kunden")
                .param("id", "1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.kundeId").value(1))
            .andExpect(jsonPath("$.vorname").value("Max"));
    }

    @Test
    void getKunden_withEmail() throws Exception {
        Kunde k = new Kunde();
        k.setKundeId(1);
        k.setEmail("test@test.de");

        when(service.findByEmail("test@test.de")).thenReturn(k);

        mvc.perform(get("/kunden").param("email", "test@test.de"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.kundeId").value(1))
            .andExpect(jsonPath("$.email").value("test@test.de"));
    }

    @Test
    void getKunden_notFound() throws Exception {
        when(service.findById(99)).thenThrow(new KundeNotFoundException(99));

        mvc.perform(get("/kunden")
                .param("id", "99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void createKunden_returnsCreated() throws Exception {
        Kunde saved = new Kunde();
        saved.setKundeId(1);
        saved.setVorname("Peter");

        when(service.create(any(Kunde.class))).thenReturn(saved);

        mvc.perform(post("/kunden")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "email": "peter@test.de",
                        "vorname": "Peter",
                        "nachname": "Paul",
                        "passwort": "123#a"
                    }
                """))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.kundeId").value(1))
            .andExpect(jsonPath("$.vorname").value("Peter"));
    }

    @Test
    void createKunden_returnsBadRequest() throws Exception {

        mvc.perform(post("/kunden")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "email": "@@@@",
                        "vorname": "Peter",
                        "nachname": "Paul",
                        "passwort": "123#a"
                    }
                """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateKunde_success() throws Exception {
        Kunde k = new Kunde();
        k.setKundeId(1);
        k.setVorname("Peter");

        when(service.update(eq(1), any(Kunde.class))).thenReturn(k);

        mvc.perform(put("/kunden")
                    .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "kundeId": 1,
                        "vorname": "Peter"
                    }
                """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.kundeId").value(1))
            .andExpect(jsonPath("$.vorname").value("Peter"));
    }

    @Test
    void updateKunde_idMismatch() throws Exception {
        doThrow(new IllegalArgumentException("Kunden-ID im Pfad und Body müssen übereinstimmen!"))
            .when(service).update(eq(1), any(Kunde.class));

        mvc.perform(put("/kunden")
                    .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "kundeId": 2,
                        "vorname": "Peter"
                    }
                """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateKunde_notFound_returns404() throws Exception {
        doThrow(new KundeNotFoundException(99)).when(service).update(eq(99), any(Kunde.class));

        mvc.perform(put("/kunden")
                    .param("id", "99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "kundeId": 99,
                        "vorname": "Peter"
                    }
                """))
            .andExpect(status().isNotFound());
    }
}
