package simpleshopapi.service;

import org.springframework.stereotype.Service;
import simpleshopapi.exception.NotFoundException;
import simpleshopapi.model.Kunde;
import simpleshopapi.repositories.KundenRepository;

import java.util.List;

@Service
public class KundenService {

    private final KundenRepository repository;

    public KundenService(KundenRepository repository) {
        this.repository = repository;
    }

    public List<Kunde> findAll() {
        return repository.findAll();
    }

    public Kunde findById(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Kunde with id " + id + " not found!"));
    }

    public Kunde findByEmail(String email) {
        return repository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("Kunde with email " + email + " not found!"));
    }

    public Kunde create(Kunde kunde) {
        return repository.createKunde(kunde);
    }

    public Kunde update(Integer id, Kunde kunde) {
        if (!id.equals(kunde.getKundeId())) {
            throw new IllegalArgumentException("Kunden ID im Pfad und Body müssen übereinstimmen!");
        }

        int updated = repository.updateKunde(kunde);
        if (updated == 0) {
            throw new NotFoundException("Kunde with id " + id + " not found!");
        }

        return kunde;
    }
}
