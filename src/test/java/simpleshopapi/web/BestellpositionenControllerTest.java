package simpleshopapi.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import simpleshopapi.controller.BestellpositionenController;
import simpleshopapi.exception.BestellpositionNotFoundException;
import simpleshopapi.model.Bestellpositionen;
import simpleshopapi.service.BestellpositionenService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BestellpositionenController.class)
public class BestellpositionenControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BestellpositionenService service;

    @Test
    void getBestellpositionen() throws Exception {
        Bestellpositionen bp = new Bestellpositionen();
        bp.setPositionsId(1);
        bp.setMenge(5);

        when(service.findAll()).thenReturn(List.of(bp));

        mvc.perform(get("/bestellpositionen"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].positionsId").value(1))
            .andExpect(jsonPath("$[0].menge").value(5));
    }

    @Test
    void getBestellpositionen_withId() throws Exception {
        Bestellpositionen bp = new Bestellpositionen();
        bp.setPositionsId(1);
        bp.setMenge(5);

        when(service.findById(1)).thenReturn(bp);

        mvc.perform(get("/bestellpositionen").param("id", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.positionsId").value(1))
            .andExpect(jsonPath("$.menge").value(5));
    }

    @Test
    void getBestellpositionen_notFound() throws Exception {
        when(service.findById(99)).thenThrow(new BestellpositionNotFoundException(99));

        mvc.perform(get("/bestellpositionen").param("id", "99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void saveBestellpositionen_success() throws Exception {
        Bestellpositionen bp = new Bestellpositionen();
        bp.setPositionsId(1);
        bp.setMenge(3);

        when(service.create(1, "SKU-0000", 3)).thenReturn(bp);

        mvc.perform(post("/bestellpositionen")
                .param("bestellungId", "1")
                .param("sku", "SKU-0000")
                .param("menge", "3"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.positionsId").value(1))
            .andExpect(jsonPath("$.menge").value(3));
    }

    @Test
    void saveBestellpositionen_missingParam() throws Exception {
        doThrow(new IllegalArgumentException("Menge ist null!"))
                .when(service).create(1, "SKU-0000", null);

        mvc.perform(post("/bestellpositionen")
                .param("bestellungId", "1")
                .param("sku", "SKU-0000"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void deleteBestellpositionen_success() throws Exception {
        doNothing().when(service).delete(1);

        mvc.perform(delete("/bestellpositionen").param("id", "1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteBestellpositionen_notFound() throws Exception {
        doThrow(new BestellpositionNotFoundException(99)).when(service).delete(99);

        mvc.perform(delete("/bestellpositionen").param("id", "99"))
            .andExpect(status().isNotFound());
    }
}
