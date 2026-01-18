package simpleshopapi.controller;

import simpleshopapi.model.KundeSummeAnzahlBestellungDTO;
import simpleshopapi.model.MitarbeiterUebersichtDTO;
import simpleshopapi.model.ProduktVerkaufszahlenDTO;
import simpleshopapi.model.MitarbeiterBestellstatusDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import simpleshopapi.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    @GetMapping("/kunde/summe-anzahl-bestellungen")
    public List<KundeSummeAnzahlBestellungDTO> getAllKundeSummeAnzahlBestellung() {
        return service.getKundeSummeAnzahlBestellung();
    }

    @GetMapping("/produkt/verkaufszahlen")
    public List<ProduktVerkaufszahlenDTO> getAllProduktVerkaufszahlen() {
        return service.getProduktVerkaufszahlen();
    }

    @GetMapping("/mitarbeiter/uebersicht")
    public List<MitarbeiterUebersichtDTO> getAllMitarbeiterUebersicht() {
        return service.getMitarbeiterUebersicht();
    }

    @GetMapping("/mitarbeiter/bestellstatus-uebersicht")
    public List<MitarbeiterBestellstatusDTO> getAllMitarbeiterBestellstatus() {
        return service.getMitarbeiterBestellstatus();
    }
}
