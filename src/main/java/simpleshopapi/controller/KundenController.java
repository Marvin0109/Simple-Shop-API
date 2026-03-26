package simpleshopapi.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import simpleshopapi.dto.LoadKundeDTO;
import simpleshopapi.model.Kunde;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simpleshopapi.service.KundenService;

import java.util.List;

@RestController
@RequestMapping("/kunden")
public class KundenController {

    private final KundenService service;

    public KundenController(KundenService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<LoadKundeDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<LoadKundeDTO> getById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<LoadKundeDTO> getByEmail(@PathVariable String email) {
        return service.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createKunden(
            @Valid @RequestBody Kunde kunde,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        LoadKundeDTO saved = service.create(kunde);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateKunde(
            @PathVariable Integer id,
            @RequestBody Kunde kunde) {

        service.update(id, kunde);
        return ResponseEntity.noContent().build();
    }
}
