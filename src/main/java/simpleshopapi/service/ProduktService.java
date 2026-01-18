package simpleshopapi.service;

import org.springframework.stereotype.Service;
import simpleshopapi.exception.ProduktNotFoundException;
import simpleshopapi.model.Produkt;
import simpleshopapi.repositories.ProduktRepository;

import java.util.List;

@Service
public class ProduktService {

    private final ProduktRepository repository;

    public ProduktService(ProduktRepository repository) {
        this.repository = repository;
    }

    public List<Produkt> findAll() {
        return repository.findAll();
    }

    public Produkt findBySku(String sku) {
        return repository.findBySKU(sku)
            .orElseThrow(() -> new ProduktNotFoundException(sku));
    }

    public Produkt create(Produkt produkt) {
        return repository.createProdukt(produkt);
    }

    public void delete(String sku) {
        boolean deleted = repository.deleteBySKU(sku);
        if (!deleted) {
            throw new ProduktNotFoundException(sku);
        }
    }

    public int updateLagerbestand(String sku, Integer lagerbestand) {
        if (sku == null || lagerbestand == null) {
            throw new IllegalArgumentException("SKU und Lagerbestand müssen gesetzt werden");
        }

        int updated = repository.updateLagerbestand(sku, lagerbestand);

        if (updated == 0) {
            throw new ProduktNotFoundException(sku);
        }

        return updated;
    }
}
