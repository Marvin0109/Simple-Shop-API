package simpleshopapi.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import simpleshopapi.model.Adresse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simpleshopapi.service.AdresseService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/adressen")
public class AdresseController {

    private final AdresseService service;

    public AdresseController(AdresseService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAdresse(@RequestParam(required = false) Integer id) {
        if (id == null) {
            return ResponseEntity.ok(service.findAll());
        }
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createAdresse(@Valid @RequestBody Adresse adresse,
                                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            bindingResult.getFieldErrors()
                    .forEach((error -> errors.add(error.getField() + ": " + error.getDefaultMessage())));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        Adresse saved = service.create(adresse);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping
    public ResponseEntity<?> updateAdresse(
            @RequestParam Integer id,
            @RequestBody Adresse adresse) {

        int updated = service.update(id, adresse);
        return ResponseEntity.ok(updated);
    }
}
