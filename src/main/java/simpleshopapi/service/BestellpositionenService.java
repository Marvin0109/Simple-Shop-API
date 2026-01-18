package simpleshopapi.service;

import org.springframework.stereotype.Service;
import simpleshopapi.exception.BestellpositionNotFoundException;
import simpleshopapi.model.Bestellpositionen;
import simpleshopapi.repositories.BestellpositionenRepository;

import java.util.List;

@Service
public class BestellpositionenService {

    private final BestellpositionenRepository repository;

    public BestellpositionenService(BestellpositionenRepository repository) {
        this.repository = repository;
    }

    public List<Bestellpositionen> findAll() {
        return repository.findAll();
    }

    public Bestellpositionen findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new BestellpositionNotFoundException(id));
    }

    public Bestellpositionen create(Integer bestellungId, String sku, Integer menge) {
        if (bestellungId == null) throw new IllegalArgumentException("Bestellung ID ist null!");
        if (sku == null) throw new IllegalArgumentException("SKU ist null!");
        if (menge == null) throw new IllegalArgumentException("Menge ist null!");

        return repository.createBestellposition(menge, sku, bestellungId);
    }

    public void delete(Integer id) {
        boolean deleted = repository.deleteById(id);
        if (!deleted) throw new BestellpositionNotFoundException(id);
    }
}
