package simpleshopapi.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import simpleshopapi.model.Mitarbeiter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simpleshopapi.service.MitarbeiterService;

import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<?> createMitarbeiter(
            @Valid @RequestBody Mitarbeiter mitarbeiter,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            bindingResult.getFieldErrors()
                    .forEach((error -> errors.add(error.getField() + ": " + error.getDefaultMessage())));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
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
