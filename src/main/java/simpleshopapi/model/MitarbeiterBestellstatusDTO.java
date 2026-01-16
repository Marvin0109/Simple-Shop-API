package simpleshopapi.model;

public record MitarbeiterBestellstatusDTO(
        Integer personalNr,
        String status,
        Integer anzahlBestellungen) {}
