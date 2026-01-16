package com.dbw.spring_boot.model;

import java.math.BigDecimal;

public class Bestellpositionen {

    private int positionsId;
    private int bestellungId;
    private String produktSku;
    private int menge;
    private BigDecimal gesamtpreis;

    public int getPositionsId() {
        return positionsId;
    }

    public void setPositionsId(int positionsId) {
        this.positionsId = positionsId;
    }

    public int getMenge() {
        return menge;
    }

    public void setMenge(int menge) {
        this.menge = menge;
    }

    public String getProduktSku() {
        return produktSku;
    }

    public void setProduktSku(String sku) {
        this.produktSku = sku;
    }

    public int getBestellungId() {
        return bestellungId;
    }

    public void setBestellungId(int bestellungId) {
        this.bestellungId = bestellungId;
    }

    public BigDecimal getGesamtpreis() {
        return gesamtpreis;
    }

    public void setGesamtpreis(BigDecimal gesamtpreis) {
        this.gesamtpreis = gesamtpreis;
    }
}
