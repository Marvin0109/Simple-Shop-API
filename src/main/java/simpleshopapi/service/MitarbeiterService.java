package simpleshopapi.service;

import org.springframework.stereotype.Service;
import simpleshopapi.exception.MitarbeiterNotFoundException;
import simpleshopapi.model.Mitarbeiter;
import simpleshopapi.repositories.MitarbeiterRepository;

import java.util.List;

@Service
public class MitarbeiterService {

    private final MitarbeiterRepository repository;

    public MitarbeiterService(MitarbeiterRepository repository) {
        this.repository = repository;
    }

    public List<Mitarbeiter> findAll() {
        return repository.findAll();
    }

    public Mitarbeiter findById(int id) {
        return repository.findById(id)
            .orElseThrow(() -> new MitarbeiterNotFoundException(id));
    }

    public Mitarbeiter create(Mitarbeiter m) {
        return repository.createMitarbeiter(m);
    }

    public void delete(int id) {
        boolean deleted = repository.deleteById(id);
        if (!deleted) {
            throw new MitarbeiterNotFoundException(id);
        }
    }
}
