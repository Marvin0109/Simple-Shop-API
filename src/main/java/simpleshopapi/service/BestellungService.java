package simpleshopapi.service;

import org.springframework.stereotype.Service;
import simpleshopapi.exception.NotFoundException;
import simpleshopapi.model.Bestellung;
import simpleshopapi.repositories.BestellungRepository;

import java.util.List;

@Service
public class BestellungService {

    private final BestellungRepository repository;

    public BestellungService(BestellungRepository repository) {
        this.repository = repository;
    }

    public List<Bestellung> findAll() {
        return repository.findAll();
    }

    public Bestellung findById(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Bestellung with id " + id + " not found!"));
    }

    public Bestellung create(Bestellung bestellung) {
        return repository.createBestellung(bestellung);
    }

    public void delete(Integer id) {
        boolean deleted = repository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Bestellung with id " + id +  " not found!");
        }
    }
}
