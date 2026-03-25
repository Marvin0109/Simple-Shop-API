package simpleshopapi.service;

import org.springframework.stereotype.Service;
import simpleshopapi.exception.NotFoundException;
import simpleshopapi.model.Adresse;
import simpleshopapi.repositories.AdresseRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AdresseService {

    private final AdresseRepository repository;

    public AdresseService(AdresseRepository repository) {
        this.repository = repository;
    }

    public List<Adresse> findAll() {
        return repository.findAll();
    }

    public Optional<Adresse> findById(Integer id) {
        return repository.findById(id);
    }

    public Adresse create(Adresse adresse) {
        return repository.save(adresse);
    }

    public void update(Integer id, Adresse adresse) {
        int updated = repository.updateAdresse(id, adresse);

        if (updated == 0) {
            throw new NotFoundException("Adresse with id " + id + " not found!");
        }
    }

    public void deleteById(Integer id) {
        int deleted = repository.deleteById(id);

        if (deleted == 0) {
            throw new NotFoundException("Adresse with id " + id + " not found!");
        }
    }
}
