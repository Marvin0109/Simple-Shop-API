package simpleshopapi.model;

public record MitarbeiterUebersichtDTO(
        Integer personalNr,
        Integer anzahlVerwalteterBestellungen,
        Integer anzahlAngelegterProdukte) {}
