package com.dbw.spring_boot.model;

import java.math.BigDecimal;

public record KundeSummeAnzahlBestellungDTO (
        Integer kundeId,
        String email,
        Integer anzahlBestellungen,
        BigDecimal gesamtsumme) {
}
