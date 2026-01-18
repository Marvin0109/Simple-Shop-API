package simpleshopapi.controller;

import org.springframework.http.HttpStatus;
import simpleshopapi.model.Mitarbeiter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simpleshopapi.service.MitarbeiterService;

@RestController
@RequestMapping("/mitarbeiter")
public class MitarbeiterController {

    private final MitarbeiterService service;

    public MitarbeiterController(MitarbeiterService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getMitarbeiter(
            @RequestParam(required = false) Integer id) {

        if (id == null) {
            return ResponseEntity.ok(service.findAll());
        }

        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Mitarbeiter> createMitarbeiter(
            @RequestBody Mitarbeiter mitarbeiter) {

        Mitarbeiter saved = service.create(mitarbeiter);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMitarbeiter(
            @RequestParam int id) {

        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
