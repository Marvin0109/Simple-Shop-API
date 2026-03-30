package simpleshopapi.service;

import org.springframework.stereotype.Service;
import simpleshopapi.exception.NotFoundException;
import simpleshopapi.model.Bestellung;
import simpleshopapi.repositories.BestellungRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BestellungService {

    private final BestellungRepository repository;

    public BestellungService(BestellungRepository repository) {
        this.repository = repository;
    }

    public List<Bestellung> findAll() {
        return repository.findAll();
    }

    public Optional<Bestellung> findById(Integer id) {
        return repository.findById(id);
    }

    public void updateStatus(Integer id, String status) {
        int updated = repository.update(id, status);

        if (updated == 0) {
            throw new NotFoundException("Bestellung with id " + id + " not found");
        }
    }

    public Bestellung create(Bestellung bestellung) {
        return repository.save(bestellung);
    }

    public void delete(Integer id) {
        int deleted = repository.deleteById(id);
        if (deleted == 0) {
            throw new NotFoundException("Bestellung with id " + id +  " not found!");
        }
    }
}
