package simpleshopapi.service;

import org.springframework.stereotype.Service;
import simpleshopapi.exception.NotFoundException;
import simpleshopapi.model.KundenAdresse;
import simpleshopapi.repositories.KundenAdresseRepository;

@Service
public class KundenAdresseService {

    private final KundenAdresseRepository repository;

    public KundenAdresseService(KundenAdresseRepository repository) {
        this.repository = repository;
    }

    public void create(KundenAdresse kundenAdresse) {
        repository.save(kundenAdresse);
    }

    public void updateType(String typ, KundenAdresse kundenAdresse) {
        if (typ.isBlank()) {
            throw new IllegalArgumentException("Adresse type is empty");
        }

        if (!typ.equals(kundenAdresse.getTyp())) {
            int updated = repository.update(typ, kundenAdresse);
            if (updated == 0) {
                throw new NotFoundException("Not able to update type");
            }
        }
    }
}
