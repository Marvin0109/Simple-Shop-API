package com.dbw.spring_boot.model;

public record MitarbeiterBestellstatusDTO(
        Integer personalNr,
        String status,
        Integer anzahlBestellungen) {}
