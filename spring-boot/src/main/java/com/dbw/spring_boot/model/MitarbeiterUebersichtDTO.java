package com.dbw.spring_boot.model;

public record MitarbeiterUebersichtDTO(
        Integer personalNr,
        Integer anzahlVerwalteterBestellungen,
        Integer anzahlAngelegterProdukte) {}
