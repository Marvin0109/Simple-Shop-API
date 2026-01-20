package simpleshopapi.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import simpleshopapi.model.Bestellung;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simpleshopapi.service.BestellungService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bestellungen")
public class BestellungController {

    private final BestellungService service;

    public BestellungController(BestellungService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getBestellungen(@RequestParam(required = false) Integer id) {
        if (id == null) {
            return ResponseEntity.ok(service.findAll());
        }
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createBestellungen(@Valid @RequestBody Bestellung bestellung,
                                                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            bindingResult.getFieldErrors()
                    .forEach((error -> errors.add(error.getField() + ": " + error.getDefaultMessage())));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        Bestellung saved = service.create(bestellung);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBestellungen(@RequestParam Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
