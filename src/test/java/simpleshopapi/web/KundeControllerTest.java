package simpleshopapi.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import simpleshopapi.controller.KundenController;
import simpleshopapi.dto.LoadKundeDTO;
import simpleshopapi.exception.NotFoundException;
import simpleshopapi.model.Kunde;
import simpleshopapi.service.KundenService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KundenController.class)
class KundeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private KundenService service;

    @Test
    void getKunden() throws Exception {
        LoadKundeDTO k = new LoadKundeDTO(
                1,
                "",
                "Max",
                ""
        );

        when(service.findAll()).thenReturn(List.of(k));

        mvc.perform(get("/kunden"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].kundeId").value(1))
            .andExpect(jsonPath("$[0].vorname").value("Max"))
            .andExpect(jsonPath("$[0].passwort").doesNotExist());
    }

    @Test
    void getKunden_withId() throws Exception {
        LoadKundeDTO k = new LoadKundeDTO(
                1,
                "",
                "Max",
                ""
        );

        when(service.findById(1)).thenReturn(Optional.of(k));

        mvc.perform(get("/kunden/id/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.kundeId").value(1))
            .andExpect(jsonPath("$.vorname").value("Max"))
            .andExpect(jsonPath("$.passwort").doesNotExist());
    }

    @Test
    void getKunden_withEmail() throws Exception {
        LoadKundeDTO k = new LoadKundeDTO(
                1,
                "test@test.de",
                "",
                ""
        );

        when(service.findByEmail("test@test.de")).thenReturn(Optional.of(k));

        mvc.perform(get("/kunden/email/{email}", "test@test.de"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.kundeId").value(1))
            .andExpect(jsonPath("$.email").value("test@test.de"))
            .andExpect(jsonPath("$.passwort").doesNotExist());
    }

    @Test
    void getKunden_notFound() throws Exception {
        when(service.findById(99)).thenThrow(new NotFoundException("Kunde with id " + 99 + " not found!"));

        mvc.perform(get("/kunden/id/{id}", 99))
            .andExpect(status().isNotFound());
    }

    @Test
    void createKunden_returnsCreated() throws Exception {
        LoadKundeDTO saved = new LoadKundeDTO(
                1,
                "",
                "Peter",
                ""
        );

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
            .andExpect(jsonPath("$.vorname").value("Peter"))
            .andExpect(jsonPath("$.passwort").doesNotExist());
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

        mvc.perform(put("/kunden/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "kundeId": 1,
                        "vorname": "Peter"
                    }
                """))
            .andExpect(status().isNoContent());
    }

    @Test
    void updateKunde_notFound_returns404() throws Exception {
        doThrow(new NotFoundException("Kunde with id " + 99 + " not found!"))
                .when(service).update(eq(99), any(Kunde.class));

        mvc.perform(put("/kunden/{id}", 99)
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
