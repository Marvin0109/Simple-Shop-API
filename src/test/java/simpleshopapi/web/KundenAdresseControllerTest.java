package simpleshopapi.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import simpleshopapi.controller.KundenAdresseController;
import simpleshopapi.exception.NotFoundException;
import simpleshopapi.model.KundenAdresse;
import simpleshopapi.service.KundenAdresseService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KundenAdresseController.class)
class KundenAdresseControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private KundenAdresseService service;

    @Test
    void createKundenAdresse_success() throws Exception {
        KundenAdresse saved = new KundenAdresse();
        saved.setKundenId(1);
        saved.setAdresseId(1);
        saved.setTyp("Lieferadresse");

        when(service.create(any())).thenReturn(saved);

        mvc.perform(post("/kunden/adresse")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "kundenId": 1,
                        "adresseId": 1,
                        "typ": "Lieferadresse"
                    }
                """))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.kundenId").value(1))
            .andExpect(jsonPath("$.adresseId").value(1))
            .andExpect(jsonPath("$.typ").value("Lieferadresse"));
    }

    @Test
    void createKundenAdresse_fail() throws Exception {
        mvc.perform(post("/kunden/adresse")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "kundenId": 1,
                        "adresseId": 1,
                        "typ": ""
                    }
                """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateKundenAdresse_success() throws Exception {
        mvc.perform(put("/kunden/adresse/updateType/{type}", "Rechnungsadresse")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "kundenId": 1,
                        "adresseId": 1,
                        "typ": "Lieferadresse"
                    }
                """))
            .andExpect(status().isNoContent());
    }

    @Test
    void updateKundenAdresse_fail() throws Exception {
        doThrow(new NotFoundException("KundenAdresse not found!"))
                .when(service).updateType(eq("Rechnungsadresse"), any(KundenAdresse.class));

        mvc.perform(put("/kunden/adresse/updateType/{type}", "Rechnungsadresse")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "kundenId": 100,
                        "adresseId": 1,
                        "typ": "Lieferadresse"
                    }
                """))
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteKundenAdresse_success() throws Exception {
        doNothing().when(service).delete(any(KundenAdresse.class));

        mvc.perform(delete("/kunden/adresse")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "kundenId": 1,
                        "adresseId": 1,
                        "typ": "Lieferadresse"
                    }
                """))
            .andExpect(status().isNoContent());
    }
}
