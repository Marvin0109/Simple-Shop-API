package simpleshopapi.controller;

import simpleshopapi.model.Kunde;
import simpleshopapi.model.KundeLogin;
import simpleshopapi.model.Mitarbeiter;
import simpleshopapi.model.MitarbeiterLogin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import simpleshopapi.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService service;

    public LoginController(LoginService service) {
        this.service = service;
    }

    @PostMapping("/mitarbeiter")
    public ResponseEntity<?> loginMitarbeiter(
            @RequestBody MitarbeiterLogin mitarbeiterLogin) {
        Mitarbeiter mitarbeiter = service.loginMitarbeiter(mitarbeiterLogin);
        return ResponseEntity.ok(mitarbeiter);
    }

    @PostMapping("/kunde")
    public ResponseEntity<?> loginKunde(@RequestBody KundeLogin kundeLogin) {
        Kunde kunde = service.loginKunde(kundeLogin);
        return ResponseEntity.ok(kunde);
    }
}
