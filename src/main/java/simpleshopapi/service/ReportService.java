package simpleshopapi.service;

import org.springframework.stereotype.Service;
import simpleshopapi.model.KundeSummeAnzahlBestellungDTO;
import simpleshopapi.model.MitarbeiterBestellstatusDTO;
import simpleshopapi.model.MitarbeiterUebersichtDTO;
import simpleshopapi.model.ProduktVerkaufszahlenDTO;
import simpleshopapi.repositories.KundeUebersichtRepository;
import simpleshopapi.repositories.MitarbeiterUebersichtRepository;
import simpleshopapi.repositories.ProduktUebersichtRepository;

import java.util.List;

@Service
public class ReportService {

    private final KundeUebersichtRepository kundeRepo;
    private final ProduktUebersichtRepository produktRepo;
    private final MitarbeiterUebersichtRepository mitarbeiterRepo;

    public ReportService(KundeUebersichtRepository kundeRepo,
                         ProduktUebersichtRepository produktRepo,
                         MitarbeiterUebersichtRepository mitarbeiterRepo) {
        this.kundeRepo = kundeRepo;
        this.produktRepo = produktRepo;
        this.mitarbeiterRepo = mitarbeiterRepo;
    }

    public List<KundeSummeAnzahlBestellungDTO> getKundeSummeAnzahlBestellung() {
        return kundeRepo.findAll();
    }

    public List<ProduktVerkaufszahlenDTO> getProduktVerkaufszahlen() {
        return produktRepo.findAll();
    }

    public List<MitarbeiterUebersichtDTO> getMitarbeiterUebersicht() {
        return mitarbeiterRepo.findAll();
    }

    public List<MitarbeiterBestellstatusDTO> getMitarbeiterBestellstatus() {
        return mitarbeiterRepo.findAllBestellStatus();
    }
}
