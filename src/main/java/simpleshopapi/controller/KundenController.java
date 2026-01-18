package simpleshopapi.controller;

import org.springframework.http.HttpStatus;
import simpleshopapi.model.Kunde;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simpleshopapi.service.KundenService;

@RestController
@RequestMapping("/kunden")
public class KundenController {

    private final KundenService service;

    public KundenController(KundenService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getKunden(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String email) {

        if (id != null) {
            return ResponseEntity.ok(service.findById(id));
        }

        if (email != null) {
            return ResponseEntity.ok(service.findByEmail(email));
        }

        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<Kunde> createKunden(@RequestBody Kunde kunde) {
        Kunde saved = service.create(kunde);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping
    public ResponseEntity<?> updateKunde(
            @RequestParam Integer id,
            @RequestBody Kunde kunde) {

        Kunde updated = service.update(id, kunde);
        return ResponseEntity.ok(updated);
    }
}
