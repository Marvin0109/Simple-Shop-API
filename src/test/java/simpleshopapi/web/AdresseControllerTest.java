package simpleshopapi.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import simpleshopapi.controller.AdresseController;
import simpleshopapi.exception.NotFoundException;
import simpleshopapi.model.Adresse;
import simpleshopapi.service.AdresseService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdresseController.class)
class AdresseControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AdresseService service;

    @Test
    void getAdresse() throws Exception {
        Adresse a = new Adresse();
        a.setAdresseId(1);
        a.setStrasse("Musterstrasse");
        when(service.findAll()).thenReturn(List.of(a));

        mvc.perform(get("/adressen"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].adresseId").value(1))
            .andExpect(jsonPath("$[0].strasse").value("Musterstrasse"));
    }

    @Test
    void getAdresse_withId() throws Exception {
        Adresse a = new Adresse();
        a.setAdresseId(1);
        a.setStrasse("Musterstraße");

        when(service.findById(1)).thenReturn(Optional.of(a));

        mvc.perform(get("/adressen/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.adresseId").value(1))
            .andExpect(jsonPath("$.strasse").value("Musterstraße"));
    }

    @Test
    void getAdresse_notFound_returns404() throws Exception {
        when(service.findById(99)).thenReturn(Optional.empty());

        mvc.perform(get("/adressen/{id}", 99))
            .andExpect(status().isNotFound());
    }

    @Test
    void createAdresse_returnsCreated() throws Exception {
        Adresse saved = new Adresse();
        saved.setAdresseId(1);
        saved.setStrasse("Hauptstraße");

        when(service.create(any(Adresse.class))).thenReturn(saved);

        mvc.perform(post("/adressen")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "aktiv": false,
                        "strasse": "Hauptstraße",
                        "hausnummer": "1",
                        "plz": "10115",
                        "ort": "Berlin",
                        "land": "Deutschland"
                    }
                """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.adresseId").value(1))
            .andExpect(jsonPath("$.strasse").value("Hauptstraße"));
    }

    @Test
    void updateAdresse_success() throws Exception {
        mvc.perform(put("/adressen/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "aktiv": false,
                        "strasse": "Linienstraße",
                        "hausnummer": "1",
                        "plz": "10115",
                        "ort": "Berlin",
                        "land": "Deutschland"
                    }
                """))
            .andExpect(status().isNoContent());
    }

    @Test
    void updateAdresse_notFound() throws Exception {
        doThrow(new NotFoundException("Adresse with id " + 99 + " not found!"))
                .when(service).update(eq(99), any(Adresse.class));

        mvc.perform(put("/adressen/{id}", 99)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "aktiv": false,
                        "strasse": "Linienstraße",
                        "hausnummer": "1",
                        "plz": "10115",
                        "ort": "Berlin",
                        "land": "Deutschland"
                    }
                """))
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteAdresse_success() throws Exception {
        mvc.perform(delete("/adressen/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAdresse_notFound() throws Exception {
        doThrow(new NotFoundException("Adresse with id " + 99 + " not found!"))
                .when(service).deleteById(99);

        mvc.perform(delete("/adressen/{id}", 99))
                .andExpect(status().isNotFound());
    }
}
