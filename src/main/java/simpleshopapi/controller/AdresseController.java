package simpleshopapi.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import simpleshopapi.model.Adresse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simpleshopapi.service.AdresseService;

import java.util.List;

@RestController
@RequestMapping("/adressen")
public class AdresseController {

    private final AdresseService service;

    public AdresseController(AdresseService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Adresse>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Adresse> getById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createAdresse(
            @Valid @RequestBody Adresse adresse,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        Adresse saved = service.create(adresse);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdresse(
            @PathVariable Integer id,
            @Valid @RequestBody Adresse adresse,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        service.update(id, adresse);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdresse(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
