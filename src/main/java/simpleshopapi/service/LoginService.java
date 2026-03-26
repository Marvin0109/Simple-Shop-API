package simpleshopapi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import simpleshopapi.dto.LoadKundeDTO;
import simpleshopapi.exception.UnauthorizedException;
import simpleshopapi.model.Kunde;
import simpleshopapi.dto.KundeLoginDTO;
import simpleshopapi.model.Mitarbeiter;
import simpleshopapi.dto.MitarbeiterLoginDTO;
import simpleshopapi.repositories.KundenRepository;
import simpleshopapi.repositories.MitarbeiterRepository;

import java.util.Optional;

@Service
public class LoginService {

    private final PasswordEncoder passwordEncoder;
    private final MitarbeiterRepository mRepo;
    private final KundenRepository kRepo;

    public LoginService(MitarbeiterRepository mRepo, KundenRepository kRepo, PasswordEncoder passwordEncoder) {
        this.mRepo = mRepo;
        this.kRepo = kRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Mitarbeiter loginMitarbeiter(MitarbeiterLoginDTO login) {
        Mitarbeiter m = mRepo.login(login);
        if (m.getPersonalNr() == null) {
            throw new UnauthorizedException("Ungültiger Login-Daten für Mitarbeiter");
        }
        return m;
    }

    public LoadKundeDTO loginKunde(KundeLoginDTO login) {
        Optional<Kunde> kundeOptional = kRepo.findByEmail(login.getEmail());

        Kunde k = kundeOptional.orElseThrow(() -> new UnauthorizedException("Ungültige Login-Daten"));

        if (!passwordEncoder.matches(login.getPasswort(), k.getPasswort())) {
            throw new UnauthorizedException("Ungültige Login-Daten");
        }

        return new LoadKundeDTO(
                k.getKundeId(),
                k.getEmail(),
                k.getVorname(),
                k.getNachname()
        );
    }
}
