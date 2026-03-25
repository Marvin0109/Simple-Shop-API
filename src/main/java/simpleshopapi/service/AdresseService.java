package simpleshopapi.service;

import org.springframework.stereotype.Service;
import simpleshopapi.exception.NotFoundException;
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
            .orElseThrow(() -> new NotFoundException("Adresse with id " + id + " not found!"));
    }

    public Adresse create(Adresse adresse) {
        return repository.createAdresse(adresse);
    }

    public void update(Integer id, Adresse adresse) {
        int updated = repository.updateAdresse(adresse);

        if (updated == 0) {
            throw new NotFoundException("Adresse with id " + id + " not found!");
        }
    }
}
