package simpleshopapi.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import simpleshopapi.dto.LoadKundeDTO;
import simpleshopapi.dto.KundeLoginDTO;
import simpleshopapi.dto.LoadMitarbeiterDTO;
import simpleshopapi.model.Mitarbeiter;
import simpleshopapi.dto.MitarbeiterLoginDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import simpleshopapi.service.LoginService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService service;

    public LoginController(LoginService service) {
        this.service = service;
    }

    @PostMapping("/mitarbeiter")
    public ResponseEntity<?> loginMitarbeiter(@Valid @RequestBody MitarbeiterLoginDTO mitarbeiterLogin,
                                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        LoadMitarbeiterDTO mitarbeiter = service.loginMitarbeiter(mitarbeiterLogin);
        return ResponseEntity.ok(mitarbeiter);
    }

    @PostMapping("/kunde")
    public ResponseEntity<?> loginKunde(@Valid @RequestBody KundeLoginDTO kundeLogin,
                                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        LoadKundeDTO kunde = service.loginKunde(kundeLogin);
        return ResponseEntity.ok(kunde);
    }
}
