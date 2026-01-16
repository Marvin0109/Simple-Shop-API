package com.dbw.spring_boot.model;

import java.math.BigDecimal;

public record ProduktVerkaufszahlenDTO(
        String sku,
        String name,
        Integer gesamtVerkaufteMenge,
        BigDecimal umsatz,
        Integer anzahlBestellungen) {}
