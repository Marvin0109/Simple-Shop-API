package simpleshopapi.service;

import org.springframework.stereotype.Service;
import simpleshopapi.exception.NotFoundException;
import simpleshopapi.model.Bestellpositionen;
import simpleshopapi.repositories.BestellpositionenRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BestellpositionenService {

    private final BestellpositionenRepository repository;

    public BestellpositionenService(BestellpositionenRepository repository) {
        this.repository = repository;
    }

    public List<Bestellpositionen> findAll() {
        return repository.findAll();
    }

    public Optional<Bestellpositionen> findById(Integer id) {
        return repository.findById(id);
    }

    public Bestellpositionen create(Integer bestellungId, String sku, Integer menge) {
        return repository.createBestellposition(menge, sku, bestellungId);
    }

    public void delete(Integer id) {
        int deleted = repository.deleteById(id);
        if (deleted == 0) throw new NotFoundException("Bestellpositionen with id " + id + " not found!");
    }
}
