package simpleshopapi.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import simpleshopapi.controller.ProduktController;
import simpleshopapi.exception.ProduktNotFoundException;
import simpleshopapi.model.Produkt;
import simpleshopapi.service.ProduktService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProduktController.class)
public class ProduktControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ProduktService service;

    @Test
    void getProdukte() throws Exception{
        Produkt p = new Produkt();
        p.setSku("SKU-0000");
        p.setName("Test Produkt");

        when(service.findAll()).thenReturn(List.of(p));

        mvc.perform(get("/produkte"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].sku").value("SKU-0000"))
            .andExpect(jsonPath("$[0].name").value("Test Produkt"));
    }

    @Test
    void getProdukt_withSKU() throws Exception{
        Produkt p = new Produkt();
        p.setSku("SKU-0000");
        p.setName("Test Produkt");

        when(service.findBySku("SKU-0000")).thenReturn(p);

        mvc.perform(get("/produkte")
                .param("sku", "SKU-0000"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sku").value("SKU-0000"))
            .andExpect(jsonPath("$.name").value("Test Produkt"));
    }

    @Test
    void getProdukt_withSKU_notFound() throws Exception{
        when(service.findBySku("Invalid sku")).thenThrow(new ProduktNotFoundException("Invalid sku"));

        mvc.perform(get("/produkte").param("sku", "Invalid sku"))
            .andExpect(status().isNotFound());
    }

    @Test
    void createProdukt_returnsCreated() throws Exception{
        Produkt saved = new Produkt();
        saved.setSku("SKU-1000");
        saved.setName("Test Produkt");
        saved.setPreis(BigDecimal.valueOf(999.99));
        saved.setLagerbestand(1);
        saved.setAngelegtVon(1);

        when(service.create(any(Produkt.class))).thenReturn(saved);

        mvc.perform(post("/produkte")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "sku": "SKU-1000",
                      "name": "Test Produkt",
                      "preis": 999.99,
                      "lagerbestand": "1",
                      "angelegtVon": 1
                    }
                """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.sku").value("SKU-1000"))
            .andExpect(jsonPath("$.name").value("Test Produkt"));
    }

    @Test
    void createProdukt_returnsBadRequest() throws Exception{

        mvc.perform(post("/produkte")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "sku": "SKU-",
                      "name": "Test Produkt",
                      "preis": 999.99,
                      "lagerbestand": "1",
                      "angelegtVon": 1
                    }
                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteProdukt_existing_returnsNoContent() throws Exception{
        doNothing().when(service).delete("SKU-0000");

        mvc.perform(delete("/produkte").param("sku", "SKU-0000"))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteProdukt_notFound_returns404() throws Exception {
        doThrow(new ProduktNotFoundException("Invalid SKU")).when(service).delete("Invalid SKU");

        mvc.perform(delete("/produkte")
                .param("sku", "Invalid SKU"))
            .andExpect(status().isNotFound());
    }

    @Test
    void updateLagerbestand_success_returnsUpdated() throws Exception {
        when(service.updateLagerbestand("SKU-0000", 10)).thenReturn(1);

        mvc.perform(put("/produkte")
            .param("sku", "SKU-0000")
            .param("lagerbestand", "10"))
            .andExpect(status().isOk())
            .andExpect(content().string("1"));
    }

    @Test
    void updateLagerbestand_nullParam_returns400() throws Exception {
        doThrow(new IllegalArgumentException("SKU und Lagerbestand müssen gesetzt werden"))
            .when(service).updateLagerbestand("SKU-0000", null);

        mvc.perform(put("/produkte")
                .param("sku", "SKU-0000"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateLagerbestand_notFound_returns404() throws Exception {
        doThrow(new ProduktNotFoundException("Invalid sku"))
            .when(service).updateLagerbestand("Invalid sku", 5);

        mvc.perform(put("/produkte")
                .param("sku", "Invalid sku")
                .param("lagerbestand", "5"))
            .andExpect(status().isNotFound());
    }
}
