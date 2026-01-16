package simpleshopapi.model;

import java.math.BigDecimal;

public class Produkt {

    private String sku;
    private String name;
    private BigDecimal preis;
    private int lagerbestand;
    private int angelegtVon;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPreis() {
        return preis;
    }

    public void setPreis(BigDecimal preis) {
        this.preis = preis;
    }

    public int getLagerbestand() {
        return lagerbestand;
    }

    public void setLagerbestand(int lagerbestand) {
        this.lagerbestand = lagerbestand;
    }

    public int getAngelegtVon() {
        return angelegtVon;
    }

    public void setAngelegtVon(int angelegtVon) {
        this.angelegtVon = angelegtVon;
    }
}
