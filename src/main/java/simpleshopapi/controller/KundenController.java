package simpleshopapi.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import simpleshopapi.model.Kunde;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simpleshopapi.service.KundenService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/kunden")
public class KundenController {

    private final KundenService service;

    public KundenController(KundenService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getKunden(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String email) {

        if (id != null) {
            return ResponseEntity.ok(service.findById(id));
        }

        if (email != null) {
            return ResponseEntity.ok(service.findByEmail(email));
        }

        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createKunden(@Valid @RequestBody Kunde kunde,
                                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            bindingResult.getFieldErrors()
                    .forEach((error -> errors.add(error.getField() + ": " + error.getDefaultMessage())));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        Kunde saved = service.create(kunde);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping
    public ResponseEntity<?> updateKunde(
            @RequestParam Integer id,
            @RequestBody Kunde kunde) {

        Kunde updated = service.update(id, kunde);
        return ResponseEntity.ok(updated);
    }
}
