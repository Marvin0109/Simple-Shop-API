package simpleshopapi.dto;

public record MitarbeiterBestellstatusDTO(
        Integer personalNr,
        String status,
        Integer anzahlBestellungen) {}
