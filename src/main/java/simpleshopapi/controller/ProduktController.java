package simpleshopapi.controller;

import simpleshopapi.model.Produkt;
import simpleshopapi.repositories.ProduktRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produkte")
public class ProduktController {

    private final ProduktRepository repository;

    public ProduktController(ProduktRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(required = false) String sku) {
        if (sku != null) {
            return repository.findBySKU(sku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.ok(repository.findAll());
        }
    }

    @PostMapping
    public ResponseEntity<Produkt> createProdukt(@RequestBody Produkt produkt) {
        Produkt saved = repository.createProdukt(produkt);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProdukt(@RequestParam String sku) {
        boolean deleted = repository.deleteBySKU(sku);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else  {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<?> updateLagerbestand(
            @RequestParam String sku,
            @RequestParam Integer lagerbestand) {

        if (lagerbestand == null) {
            return ResponseEntity.badRequest().body("Neuer Lagerbestand muss gesetzt werden!");
        }

        if (sku == null) {
            return ResponseEntity.badRequest().body("SKU muss gesetzt werden!");
        }

        int updated = repository.updateLagerbestand(sku, lagerbestand);

        if (updated == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }
}
