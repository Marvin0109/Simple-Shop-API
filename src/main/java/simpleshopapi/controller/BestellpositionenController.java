package simpleshopapi.controller;

import org.springframework.http.HttpStatus;
import simpleshopapi.model.Bestellpositionen;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simpleshopapi.service.BestellpositionenService;

@RestController
@RequestMapping("/bestellpositionen")
public class BestellpositionenController {

    private final BestellpositionenService service;

    public BestellpositionenController(BestellpositionenService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getBestellpositionen(@RequestParam(required = false) Integer id) {
        if (id == null) {
            return ResponseEntity.ok(service.findAll());
        }
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> saveBestellpositionen(
            @RequestParam Integer bestellungId,
            @RequestParam String sku,
            @RequestParam Integer menge) {

        Bestellpositionen bp = service.create(bestellungId, sku, menge);
        return ResponseEntity.status(HttpStatus.CREATED).body(bp);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBestellpositionen(@RequestParam Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
