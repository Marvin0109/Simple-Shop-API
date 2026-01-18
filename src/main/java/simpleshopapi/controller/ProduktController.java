package simpleshopapi.controller;

import org.springframework.http.HttpStatus;
import simpleshopapi.model.Produkt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simpleshopapi.service.ProduktService;

@RestController
@RequestMapping("/produkte")
public class ProduktController {

    private final ProduktService service;

    public ProduktController(ProduktService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(required = false) String sku) {

        if (sku == null) {
            return ResponseEntity.ok(service.findAll());
        }

        return ResponseEntity.ok(service.findBySku(sku));
    }

    @PostMapping
    public ResponseEntity<Produkt> createProdukt(@RequestBody Produkt produkt) {

        Produkt saved = service.create(produkt);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProdukt(@RequestParam String sku) {

        service.delete(sku);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<?> updateLagerbestand(
            @RequestParam String sku,
            @RequestParam Integer lagerbestand) {

        int updated = service.updateLagerbestand(sku, lagerbestand);
        return ResponseEntity.ok(updated);
    }
}
