package simpleshopapi.controller;

import simpleshopapi.model.Mitarbeiter;
import simpleshopapi.repositories.MitarbeiterRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mitarbeiter")
public class MitarbeiterController {

    private final MitarbeiterRepository repository;

    public MitarbeiterController(MitarbeiterRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<?> getMitarbeiter(@RequestParam(required = false) Integer id) {
        if (id != null) {
            return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.ok(repository.findAll());
        }
    }

    @PostMapping
    public ResponseEntity<Mitarbeiter> createMitarbeiter(@RequestBody Mitarbeiter mitarbeiter) {
        Mitarbeiter saved = repository.createMitarbeiter(mitarbeiter);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMitarbeiter(@RequestParam int id) {
        boolean deleted = repository.deleteById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
