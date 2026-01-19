package simpleshopapi.service;

import org.springframework.stereotype.Service;
import simpleshopapi.exception.UnauthorizedException;
import simpleshopapi.model.Kunde;
import simpleshopapi.dto.KundeLoginDTO;
import simpleshopapi.model.Mitarbeiter;
import simpleshopapi.dto.MitarbeiterLoginDTO;
import simpleshopapi.repositories.KundenRepository;
import simpleshopapi.repositories.MitarbeiterRepository;

@Service
public class LoginService {

    private final MitarbeiterRepository mRepo;
    private final KundenRepository kRepo;

    public LoginService(MitarbeiterRepository mRepo, KundenRepository kRepo) {
        this.mRepo = mRepo;
        this.kRepo = kRepo;
    }

    public Mitarbeiter loginMitarbeiter(MitarbeiterLoginDTO login) {
        Mitarbeiter m = mRepo.login(login);
        if (m.getPersonalNr() == null) {
            throw new UnauthorizedException("Ungültiger Login-Daten für Mitarbeiter");
        }
        return m;
    }

    public Kunde loginKunde(KundeLoginDTO login) {
        Kunde k = kRepo.login(login);
        if (k.getKundeId() == null) {
            throw new UnauthorizedException("Ungültige Login-Daten für Kunde");
        }
        return k;
    }
}
