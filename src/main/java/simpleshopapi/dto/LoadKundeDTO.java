package simpleshopapi.dto;

import java.util.List;

public record LoadKundeDTO (
        Integer kundeId,
        String email,
        String vorname,
        String nachname,
        List<AdresseMitTypDTO> adresseMitTyp) {
}
