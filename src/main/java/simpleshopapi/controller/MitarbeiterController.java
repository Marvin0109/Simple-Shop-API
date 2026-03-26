package simpleshopapi.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import simpleshopapi.dto.LoadMitarbeiterDTO;
import simpleshopapi.model.Mitarbeiter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simpleshopapi.service.MitarbeiterService;

import java.util.List;

@RestController
@RequestMapping("/mitarbeiter")
public class MitarbeiterController {

    private final MitarbeiterService service;

    public MitarbeiterController(MitarbeiterService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<LoadMitarbeiterDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{personalNr}")
    public ResponseEntity<LoadMitarbeiterDTO> getMitarbeiter(
            @PathVariable Integer personalNr) {
        return service.findById(personalNr)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createMitarbeiter(
            @Valid @RequestBody Mitarbeiter mitarbeiter,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        LoadMitarbeiterDTO saved = service.create(mitarbeiter);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    @DeleteMapping("/{personalNr}")
    public ResponseEntity<Void> deleteMitarbeiter(
            @PathVariable Integer personalNr) {

        service.delete(personalNr);
        return ResponseEntity.noContent().build();
    }
}
