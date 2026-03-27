package simpleshopapi.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import simpleshopapi.model.Bestellpositionen;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simpleshopapi.service.BestellpositionenService;

import java.util.List;

@RestController
@RequestMapping("/bestellpositionen")
public class BestellpositionenController {

    private final BestellpositionenService service;

    public BestellpositionenController(BestellpositionenService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Bestellpositionen>> getAllBestellpositionen() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> saveBestellpositionen(
            @Valid @RequestBody Bestellpositionen pos,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        Bestellpositionen bp = service.create(
                pos.getBestellungId(),
                pos.getProduktSku(),
                pos.getMenge());

        return ResponseEntity.status(HttpStatus.CREATED).body(bp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBestellpositionen(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
