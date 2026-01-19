package simpleshopapi.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import simpleshopapi.controller.ReportController;
import simpleshopapi.dto.KundeSummeAnzahlBestellungDTO;
import simpleshopapi.dto.MitarbeiterBestellstatusDTO;
import simpleshopapi.dto.MitarbeiterUebersichtDTO;
import simpleshopapi.dto.ProduktVerkaufszahlenDTO;
import simpleshopapi.service.ReportService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
public class ReportControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ReportService service;

    @Test
    void getKundeSummeAnzahlBestellung() throws Exception {
        KundeSummeAnzahlBestellungDTO dto = new KundeSummeAnzahlBestellungDTO(
                1, "", 5,
                BigDecimal.valueOf(0.00));

        when(service.getKundeSummeAnzahlBestellung()).thenReturn(List.of(dto));

        mvc.perform(get("/report/kunde/summe-anzahl-bestellungen"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].kundeId").value(1))
            .andExpect(jsonPath("$[0].anzahlBestellungen").value(5));
    }

    @Test
    void getProduktVerkaufszahlen() throws Exception {
        ProduktVerkaufszahlenDTO dto = new ProduktVerkaufszahlenDTO(
                "SKU-0000", "", 10,
                BigDecimal.valueOf(0.00), 0);

        when(service.getProduktVerkaufszahlen()).thenReturn(List.of(dto));

        mvc.perform(get("/report/produkt/verkaufszahlen"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].sku").value("SKU-0000"))
            .andExpect(jsonPath("$[0].gesamtVerkaufteMenge").value(10));
    }

    @Test
    void getMitarbeiterUebersicht() throws Exception {
        MitarbeiterUebersichtDTO dto = new MitarbeiterUebersichtDTO(1, 2, 3);

        when(service.getMitarbeiterUebersicht()).thenReturn(List.of(dto));

        mvc.perform(get("/report/mitarbeiter/uebersicht"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].personalNr").value(1))
            .andExpect(jsonPath("$[0].anzahlVerwalteterBestellungen").value(2))
            .andExpect(jsonPath("$[0].anzahlAngelegterProdukte").value(3));
    }

    @Test
    void getMitarbeiterBestellstatus() throws Exception {
        MitarbeiterBestellstatusDTO dto = new MitarbeiterBestellstatusDTO(2, "neu", 1);

        when(service.getMitarbeiterBestellstatus()).thenReturn(List.of(dto));

        mvc.perform(get("/report/mitarbeiter/bestellstatus-uebersicht"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].personalNr").value(2))
            .andExpect(jsonPath("$[0].status").value("neu"))
            .andExpect(jsonPath("$[0].anzahlBestellungen").value(1));
    }
}
