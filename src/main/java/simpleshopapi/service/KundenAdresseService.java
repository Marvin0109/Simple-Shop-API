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

    public KundenAdresse create(KundenAdresse kundenAdresse) {
        return repository.save(kundenAdresse);
    }

    public void updateType(String typ, KundenAdresse kundenAdresse) {
        if (typ.isBlank()) {
            throw new IllegalArgumentException("Adresse type is empty");
        }

        if (!typ.equals(kundenAdresse.getTyp())) {
            int updated = repository.update(typ, kundenAdresse);
            if (updated == 0) {
                throw new NotFoundException("Not able to update typ");
            }
        }
    }

    public void delete(KundenAdresse kundenAdresse) {
        int deleted = repository.delete(kundenAdresse);

        if (deleted == 0) {
            throw new NotFoundException("KundenAdresse not found!");
        }
    }
}
