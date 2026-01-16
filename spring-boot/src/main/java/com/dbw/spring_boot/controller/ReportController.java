package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.KundeSummeAnzahlBestellungDTO;
import com.dbw.spring_boot.model.MitarbeiterUebersichtDTO;
import com.dbw.spring_boot.model.ProduktVerkaufszahlenDTO;
import com.dbw.spring_boot.repositories.KundeUebersichtRepository;
import com.dbw.spring_boot.model.MitarbeiterBestellstatusDTO;
import com.dbw.spring_boot.repositories.MitarbeiterUebersichtRepository;
import com.dbw.spring_boot.repositories.ProduktUebersichtRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final KundeUebersichtRepository kundeViewRepo;
    private final ProduktUebersichtRepository produktViewRepo;
    private final MitarbeiterUebersichtRepository mitarbeiterViewRepo;

    public ReportController(KundeUebersichtRepository kundeViewRepo,
                            ProduktUebersichtRepository produktViewRepo,
                            MitarbeiterUebersichtRepository mitarbeiterViewRepo) {
        this.kundeViewRepo = kundeViewRepo;
        this.produktViewRepo = produktViewRepo;
        this.mitarbeiterViewRepo = mitarbeiterViewRepo;
    }

    @GetMapping("/kunde/summe-anzahl-bestellungen")
    public List<KundeSummeAnzahlBestellungDTO> getAllKundeSummeAnzahlBestellung() {
        return kundeViewRepo.findAll();
    }

    @GetMapping("/produkt/verkaufszahlen")
    public List<ProduktVerkaufszahlenDTO> getAllProduktVerkaufszahlen() {
        return produktViewRepo.findAll();
    }

    @GetMapping("/mitarbeiter/uebersicht")
    public List<MitarbeiterUebersichtDTO> getAllMitarbeiterUebersicht() {
        return mitarbeiterViewRepo.findAll();
    }

    @GetMapping("/mitarbeiter/bestellstatus-uebersicht")
    public List<MitarbeiterBestellstatusDTO> getAllMitarbeiterBestellstatus() {
        return mitarbeiterViewRepo.findAllBestellStatus();
    }
}
