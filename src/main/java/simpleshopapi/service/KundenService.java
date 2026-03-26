package simpleshopapi.service;

import org.springframework.stereotype.Service;
import simpleshopapi.dto.LoadKundeDTO;
import simpleshopapi.exception.NotFoundException;
import simpleshopapi.model.Kunde;
import simpleshopapi.repositories.KundenRepository;

import java.util.List;
import java.util.Optional;

@Service
public class KundenService {

    private final KundenRepository repository;

    public KundenService(KundenRepository repository) {
        this.repository = repository;
    }

    public List<LoadKundeDTO> findAll() {
        return repository.findAll().stream()
                .map(KundenService::map)
                .toList();
    }

    public Optional<LoadKundeDTO> findById(Integer id) {
        Optional<Kunde> kunde = repository.findById(id);
        if (kunde.isPresent()) {
            Kunde loadedKunde = kunde.get();
            return Optional.of(map(loadedKunde));
        }
        return Optional.empty();
    }

    public Optional<LoadKundeDTO> findByEmail(String email) {
        Optional<Kunde> k = repository.findByEmail(email);
        if (k.isPresent()) {
            Kunde loadKunde = k.get();
            return Optional.of(map(loadKunde));
        }
        return Optional.empty();
    }

    public LoadKundeDTO create(Kunde kunde) {
        return map(repository.save(kunde));
    }

    public void update(Integer id, Kunde kunde) {
        int updated = repository.updateKunde(kunde);

        if (updated == 0) {
            throw new NotFoundException("Kunde with id " + id + " not found!");
        }
    }

    private static LoadKundeDTO map(Kunde kunde) {
        return new LoadKundeDTO(
                kunde.getKundeId(),
                kunde.getEmail(),
                kunde.getVorname(),
                kunde.getNachname(),
                kunde.getAdressen()
        );
    }
}
