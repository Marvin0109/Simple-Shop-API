package simpleshopapi.controller;

import simpleshopapi.model.Bestellung;
import simpleshopapi.repositories.BestellungRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bestellungen")
public class BestellungController {

    private final BestellungRepository repository;

    public BestellungController(BestellungRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<?> getBestellungen(@RequestParam(required = false) Integer id) {
        if (id != null) {
            return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.ok(repository.findAll());
        }
    }

    @PostMapping
    public ResponseEntity<Bestellung> createBestellungen(@RequestBody Bestellung bestellung) {
        Bestellung saved = repository.createBestellung(bestellung);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping
    public ResponseEntity<Void>deleteBestellungen(@RequestParam Integer id) {
        boolean deleted = repository.deleteById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
