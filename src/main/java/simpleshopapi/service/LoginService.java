package simpleshopapi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import simpleshopapi.dto.LoadKundeDTO;
import simpleshopapi.dto.LoadMitarbeiterDTO;
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

    public LoadMitarbeiterDTO loginMitarbeiter(MitarbeiterLoginDTO login) {
        Optional<Mitarbeiter> mitarbeiterOptional = mRepo.findById(login.getPersonalNr());

        Mitarbeiter m =  mitarbeiterOptional.orElseThrow(() -> new UnauthorizedException("Ungültige Personalnummer"));

        if (!passwordEncoder.matches(login.getPasswort(), m.getPasswort())) {
            throw new UnauthorizedException("Ungültiges Passwort");
        }

        return new LoadMitarbeiterDTO(
                m.getPersonalNr(),
                m.getEmail(),
                m.getVorname(),
                m.getNachname()
        );
    }

    public LoadKundeDTO loginKunde(KundeLoginDTO login) {
        Optional<Kunde> kundeOptional = kRepo.findByEmail(login.getEmail());

        Kunde k = kundeOptional.orElseThrow(() -> new UnauthorizedException("Ungültige Email"));

        if (!passwordEncoder.matches(login.getPasswort(), k.getPasswort())) {
            throw new UnauthorizedException("Ungültiges Passwort");
        }

        return new LoadKundeDTO(
                k.getKundeId(),
                k.getEmail(),
                k.getVorname(),
                k.getNachname()
        );
    }
}
