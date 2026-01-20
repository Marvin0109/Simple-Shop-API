package simpleshopapi.model;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class Produkt {

    @NotNull(message = "SKU muss gesetzt werden")
    @NotBlank(message = "SKU darf nicht leer sein")
    @Pattern(regexp = "^SKU-[1-9][0-9]{3}$", message = "SKU muss im folgenden Format vorliegen: SKU-1000 aufsteigend")
    private String sku;

    @NotNull(message = "Name muss gesetzt werden")
    @NotBlank(message = "Name darf nicht leer sein")
    private String name;

    @NotNull(message = "Preis muss gesetzt werden")
    @DecimalMin(value = "0.99" , inclusive = true, message = "Preis muss mindestens 0,99 sein")
    private BigDecimal preis;

    @NotNull(message = "Lagerbestand muss gesetzt werden")
    @Min(value = 0, message = "Lagerbestand darf nicht negativ sein")
    private int lagerbestand;

    @NotNull(message = "Mitarbeiter ID muss gesetzt werden")
    @Min(value = 1, message = "Mitarbeiter ID's fangen bei 1 an")
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
