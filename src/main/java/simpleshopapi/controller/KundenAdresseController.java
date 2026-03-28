package simpleshopapi.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import simpleshopapi.model.KundenAdresse;
import simpleshopapi.service.KundenAdresseService;

import java.util.List;

@RestController
@RequestMapping("/kunden/adresse")
public class KundenAdresseController {

    private final KundenAdresseService service;

    public KundenAdresseController(KundenAdresseService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> createKundenAdresse(
            @Valid @RequestBody KundenAdresse kundenAdresse,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        service.create(kundenAdresse);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/updateType/{type}")
    public ResponseEntity<?> updateKundenAdresse(
            @Valid @RequestBody KundenAdresse kundenAdresse,
            @PathVariable String type,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        service.updateType(type, kundenAdresse);
        return ResponseEntity.noContent().build();
    }
}
