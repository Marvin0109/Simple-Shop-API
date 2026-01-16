package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Kunde;
import com.dbw.spring_boot.model.KundeLogin;
import com.dbw.spring_boot.model.Mitarbeiter;
import com.dbw.spring_boot.model.MitarbeiterLogin;
import com.dbw.spring_boot.repositories.KundenRepository;
import com.dbw.spring_boot.repositories.MitarbeiterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final MitarbeiterRepository mRepository;
    private final KundenRepository kRepository;

    public LoginController(MitarbeiterRepository mRepository, KundenRepository kRepository) {
        this.mRepository = mRepository;
        this.kRepository = kRepository;
    }

    @PostMapping("/mitarbeiter")
    public ResponseEntity<?> loginMitarbeiter(@RequestBody MitarbeiterLogin mitarbeiterLogin) {
        Mitarbeiter mitarbeiter = mRepository.login(mitarbeiterLogin);

        if (mitarbeiter.getPersonalNr() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mitarbeiter);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(mitarbeiter);
        }
    }

    @PostMapping("/kunde")
    public ResponseEntity<?> loginKunde(@RequestBody KundeLogin kundeLogin) {
        Kunde kunde = kRepository.login(kundeLogin);

        if (kunde.getKundeId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(kunde);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(kunde);
        }
    }
}
