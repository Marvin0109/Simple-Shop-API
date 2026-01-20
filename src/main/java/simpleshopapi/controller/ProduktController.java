package simpleshopapi.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import simpleshopapi.model.Produkt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simpleshopapi.service.ProduktService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/produkte")
public class ProduktController {

    private final ProduktService service;

    public ProduktController(ProduktService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(required = false) String sku) {

        if (sku == null) {
            return ResponseEntity.ok(service.findAll());
        }

        return ResponseEntity.ok(service.findBySku(sku));
    }

    @PostMapping
    public ResponseEntity<?> createProdukt(@Valid @RequestBody Produkt produkt,
                                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            bindingResult.getFieldErrors()
                    .forEach((error -> errors.add(error.getField() + ": " + error.getDefaultMessage())));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        Produkt saved = service.create(produkt);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProdukt(@RequestParam String sku) {

        service.delete(sku);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<?> updateLagerbestand(
            @RequestParam String sku,
            @RequestParam Integer lagerbestand) {

        int updated = service.updateLagerbestand(sku, lagerbestand);
        return ResponseEntity.ok(updated);
    }
}
