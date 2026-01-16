package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Kunde;
import com.dbw.spring_boot.repositories.KundenRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kunden")
public class KundenController {

    private final KundenRepository repository;

    public KundenController(KundenRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<?> getKunden(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String email) {

        if (id != null) {
            return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } else if (email != null) {
            return repository.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.ok(repository.findAll());
        }
    }

    @PostMapping
    public ResponseEntity<Kunde> createKunden(@RequestBody Kunde kunde) {
        Kunde saved = repository.createKunde(kunde);
        return ResponseEntity.ok(saved);
    }

    @PutMapping
    public ResponseEntity<?> updateKunde(
            @RequestParam Integer id,
            @RequestBody Kunde kunde) {

        if (!id.equals(kunde.getKundeId())) {
            return ResponseEntity.badRequest().body("Kunden-ID im Pfad und Body müssen übereinstimmen!");
        }

        int updated = repository.updateKunde(kunde);

        if (updated == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(kunde);
    }
}
