package simpleshopapi.controller;

import simpleshopapi.model.Adresse;
import simpleshopapi.repositories.AdresseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adressen")
public class AdresseController {

    private final AdresseRepository repository;

    public AdresseController(AdresseRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<?> getAdresse(@RequestParam(required = false) Integer id) {
        if (id != null) {
            return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.ok(repository.findAll());
        }
    }

    @PostMapping
    public ResponseEntity<?> createAdresse(@RequestBody Adresse adresse) {
        Adresse saved = repository.createAdresse(adresse);
        return ResponseEntity.ok(saved);
    }

    @PutMapping
    public ResponseEntity<?> updateAdresse(
            @RequestParam Integer id,
            @RequestBody Adresse adresse) {

        if (!id.equals(adresse.getAdresseId())) {
            return ResponseEntity.badRequest().body("Adresse-ID im Pfad und Body müssen übereinstimmen!");
        }

        int updated = repository.updateAdresse(adresse);

        if (updated == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }
}
