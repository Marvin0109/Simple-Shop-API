package simpleshopapi.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import simpleshopapi.model.Produkt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simpleshopapi.service.ProduktService;

import java.util.List;

@RestController
@RequestMapping("/produkte")
public class ProduktController {

    private final ProduktService service;

    public ProduktController(ProduktService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Produkt>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{sku}")
    public ResponseEntity<Produkt> getBySku(@PathVariable String sku) {
        return service.findBySku(sku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createProdukt(
            @Valid @RequestBody Produkt produkt,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        Produkt saved = service.create(produkt);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    @PatchMapping("/{sku}/lagerbestand")
    public ResponseEntity<Void> updateLagerbestand(
            @PathVariable String sku,
            @RequestBody Integer lagerbestand) {

        service.updateLagerbestand(sku, lagerbestand);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<Void> deleteProdukt(@PathVariable String sku) {
        service.delete(sku);
        return ResponseEntity.noContent().build();
    }
}
