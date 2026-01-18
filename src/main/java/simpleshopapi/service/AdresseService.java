package simpleshopapi.service;

import org.springframework.stereotype.Service;
import simpleshopapi.exception.AdresseNotFoundException;
import simpleshopapi.model.Adresse;
import simpleshopapi.repositories.AdresseRepository;

import java.util.List;

@Service
public class AdresseService {

    private final AdresseRepository repository;

    public AdresseService(AdresseRepository repository) {
        this.repository = repository;
    }

    public List<Adresse> findAll() {
        return repository.findAll();
    }

    public Adresse findById(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> new AdresseNotFoundException(id));
    }

    public Adresse create(Adresse adresse) {
        return repository.createAdresse(adresse);
    }

    public int update(Integer id, Adresse adresse) {
        if (!id.equals(adresse.getAdresseId())) {
            throw new IllegalArgumentException("Adresse ID im Pfad und Body müssen übereinstimmen!");
        }

        int updated = repository.updateAdresse(adresse);

        if (updated == 0) {
            throw new AdresseNotFoundException(id);
        }

        return updated;
    }
}
