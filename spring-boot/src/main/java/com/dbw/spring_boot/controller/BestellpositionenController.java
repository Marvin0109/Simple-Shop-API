package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Bestellpositionen;
import com.dbw.spring_boot.repositories.BestellpositionenRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bestellpositionen")
public class BestellpositionenController {

    private final BestellpositionenRepository repository;

    public BestellpositionenController(BestellpositionenRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<?> getBestellpositionen(@RequestParam(required = false) Integer id) {
        if (id != null) {
            return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.ok(repository.findAll());
        }
    }

    @PostMapping
    public ResponseEntity<?> saveBestellpositionen(
            @RequestParam Integer bestellungId,
            @RequestParam String sku,
            @RequestParam Integer menge) {
        if (bestellungId == null) {
            return ResponseEntity.badRequest().body("Bestellung-ID ist null!");
        }

        if (sku == null) {
            return ResponseEntity.badRequest().body("Sku ist null!");
        }

        if (menge == null) {
            return ResponseEntity.badRequest().body("Menge ist null!");
        }

        Bestellpositionen bp = repository.createBestellposition(menge, sku, bestellungId);
        return ResponseEntity.ok(bp);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBestellpositionen(@RequestParam Integer id) {
        boolean deleted = repository.deleteById(id);
        if  (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
