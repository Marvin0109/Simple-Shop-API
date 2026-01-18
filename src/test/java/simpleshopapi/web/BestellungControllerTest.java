package simpleshopapi.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import simpleshopapi.controller.BestellungController;
import simpleshopapi.exception.BestellungNotFoundException;
import simpleshopapi.model.Bestellung;
import simpleshopapi.service.BestellungService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BestellungController.class)
public class BestellungControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BestellungService service;

    @Test
    void getBestellungen() throws Exception {
        Bestellung b = new Bestellung();
        b.setBestellungId(1);
        b.setStatus("neu");

        when(service.findAll()).thenReturn(List.of(b));

        mvc.perform(get("/bestellungen"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].bestellungId").value(1))
            .andExpect(jsonPath("$[0].status").value("neu"));
    }

    @Test
    void getBestellungen_withId() throws Exception {
        Bestellung b = new Bestellung();
        b.setBestellungId(2);
        b.setStatus("neu");

        when(service.findById(2)).thenReturn(b);

        mvc.perform(get("/bestellungen").param("id", "2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.bestellungId").value(2))
            .andExpect(jsonPath("$.status").value("neu"));
    }

    @Test
    void getBestellungen_withId_notFound() throws Exception {
        when(service.findById(99)).thenThrow(new BestellungNotFoundException(99));

        mvc.perform(get("/bestellungen").param("id", "99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void createBestellungen_returnsCreated() throws Exception {
        Bestellung saved = new Bestellung();
        saved.setBestellungId(1);
        saved.setStatus("neu");
        saved.setKundeId(1);

        when(service.create(any(Bestellung.class))).thenReturn(saved);

        mvc.perform(post("/bestellungen")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "kundeId": 1,
                        "personalNr": 1,
                        "datum": "2024-01-19T14:05:00Z",
                        "status": "neu"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.kundeId").value(1))
            .andExpect(jsonPath("$.bestellungId").value(1))
            .andExpect(jsonPath("$.status").value("neu"));
    }

    @Test
    void deleteBestellungen_existing_returnsNoContent() throws Exception {
        doNothing().when(service).delete(1);

        mvc.perform(delete("/bestellungen")
                .param("id", "1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteBestellungen_notFound_returns404() throws Exception {
        doThrow(new BestellungNotFoundException(99)).when(service).delete(99);

        mvc.perform(delete("/bestellungen")
                .param("id", "99"))
            .andExpect(status().isNotFound());
    }

}
