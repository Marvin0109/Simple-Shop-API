package simpleshopapi.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import simpleshopapi.controller.AdresseController;
import simpleshopapi.exception.AdresseNotFoundException;
import simpleshopapi.model.Adresse;
import simpleshopapi.service.AdresseService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdresseController.class)
public class AdresseControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AdresseService service;

    @Test
    void getAdresse() throws Exception {
        Adresse a = new Adresse();
        a.setAdresseId(1);
        a.setStrasse("Musterstraße");

        when(service.findAll()).thenReturn(List.of(a));

        mvc.perform(get("/adressen"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].adresseId").value(1))
            .andExpect(jsonPath("$[0].strasse").value("Musterstraße"));
    }

    @Test
    void getAdresse_withId() throws Exception {
        Adresse a = new Adresse();
        a.setAdresseId(1);
        a.setStrasse("Musterstraße");

        when(service.findById(1)).thenReturn(a);

        mvc.perform(get("/adressen")
                .param("id", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.adresseId").value(1))
            .andExpect(jsonPath("$.strasse").value("Musterstraße"));
    }

    @Test
    void getAdresse_notFound_returns404() throws Exception {
        when(service.findById(99)).thenThrow(new AdresseNotFoundException(99));

        mvc.perform(get("/adressen").param("id", "99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void createAdresse_returnsCreated() throws Exception {
        Adresse saved = new Adresse();
        saved.setAdresseId(1);
        saved.setStrasse("Neue Straße");

        when(service.create(any(Adresse.class))).thenReturn(saved);

        mvc.perform(post("/adressen")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "strasse": "Neue Straße"
                    }
                """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.adresseId").value(1))
            .andExpect(jsonPath("$.strasse").value("Neue Straße"));
    }

    @Test
    void updateAdresse_success() throws Exception {
        when(service.update(eq(1), any(Adresse.class))).thenReturn(1);

        mvc.perform(put("/adressen")
                        .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "adresseId": 1,
                        "strasse": "Updated Straße"
                    }
                """))
            .andExpect(status().isOk())
            .andExpect(content().string("1"));
    }

    @Test
    void updateAdresse_idMismatch() throws Exception {
        doThrow(new IllegalArgumentException("Adresse ID im Pfad und Body müssen übereinstimmen!"))
            .when(service).update(eq(1), any(Adresse.class));

        mvc.perform(put("/adressen")
                    .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "adresseId": 2,
                        "strasse": "Updated Straße"
                    }
                """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateAdresse_notFound() throws Exception {
        doThrow(new AdresseNotFoundException(99)).when(service).update(eq(99), any(Adresse.class));

        mvc.perform(put("/adressen")
                    .param("id", "99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "adresseId": 99,
                        "strasse": "Updated Straße"
                    }
                """))
            .andExpect(status().isNotFound());
    }
}
