package simpleshopapi.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public class Bestellpositionen {

    private int positionsId;

    @NotNull(message = "Bestellung ID muss gesetzt werden")
    @NotBlank(message = "Bestellung ID darf nicht leer sein")
    private int bestellungId;

    @NotNull(message = "SKU muss gesetzt werden")
    @NotBlank(message = "SKU darf nicht leer sein")
    @Pattern(regexp = "^SKU-[1-9][0-9]{3}$", message = "SKU muss im folgenden Format vorliegen: SKU-1000 aufsteigend")
    private String produktSku;

    @NotNull(message = "Menge muss gesetzt werden")
    @NotBlank(message = "Menge darf nicht leer sein")
    @Min(value = 1, message = "Menge muss größer als 0 sein")
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
