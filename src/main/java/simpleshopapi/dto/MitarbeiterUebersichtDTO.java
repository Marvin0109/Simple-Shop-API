package simpleshopapi.dto;

public record MitarbeiterUebersichtDTO(
        Integer personalNr,
        Integer anzahlVerwalteterBestellungen,
        Integer anzahlAngelegterProdukte) {}
