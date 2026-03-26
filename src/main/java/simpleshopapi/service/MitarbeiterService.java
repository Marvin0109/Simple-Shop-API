package simpleshopapi.service;

import org.springframework.stereotype.Service;
import simpleshopapi.dto.LoadMitarbeiterDTO;
import simpleshopapi.exception.NotFoundException;
import simpleshopapi.model.Mitarbeiter;
import simpleshopapi.repositories.MitarbeiterRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MitarbeiterService {

    private final MitarbeiterRepository repository;

    public MitarbeiterService(MitarbeiterRepository repository) {
        this.repository = repository;
    }

    public List<LoadMitarbeiterDTO> findAll() {
        return repository.findAll().stream()
                .map(MitarbeiterService::map)
                .toList();
    }

    public Optional<LoadMitarbeiterDTO> findById(Integer id) {
        Optional<Mitarbeiter> mitarbeiter = repository.findById(id);
        if (mitarbeiter.isPresent()) {
            Mitarbeiter loaded = mitarbeiter.get();
            return Optional.of(map(loaded));
        }
        return Optional.empty();
    }

    public LoadMitarbeiterDTO create(Mitarbeiter m) {
        return map(repository.save(m));
    }

    public void delete(int id) {
        int deleted = repository.deleteById(id);

        if (deleted == 0) {
            throw new NotFoundException("Mitarbeiter with id " + id + " not found!");
        }
    }

    private static LoadMitarbeiterDTO map(Mitarbeiter m) {
        return new LoadMitarbeiterDTO(
                m.getPersonalNr(),
                m.getEmail(),
                m.getVorname(),
                m.getNachname()
        );
    }
}
