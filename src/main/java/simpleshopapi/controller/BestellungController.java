package simpleshopapi.controller;

import org.springframework.http.HttpStatus;
import simpleshopapi.model.Bestellung;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simpleshopapi.service.BestellungService;

@RestController
@RequestMapping("/bestellungen")
public class BestellungController {

    private final BestellungService service;

    public BestellungController(BestellungService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getBestellungen(@RequestParam(required = false) Integer id) {
        if (id == null) {
            return ResponseEntity.ok(service.findAll());
        }
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Bestellung> createBestellungen(@RequestBody Bestellung bestellung) {
        Bestellung saved = service.create(bestellung);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBestellungen(@RequestParam Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
