package simpleshopapi.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import simpleshopapi.dto.PositionenFuerBestellungDTO;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class Bestellung {

    private int bestellungId;

    @NotNull(message = "Kunden ID muss gesetzt werden")
    @Min(value = 1, message = "Kunden ID's fangen bei 1 an")
    private int kundeId;

    @NotNull(message = "Personal Nr. muss gesetzt werden")
    @Min(value = 1, message = "Personal Nummern fangen bei 1 an")
    private int personalNr;

    @NotNull(message = "Datum muss gesetzt werden (Beispiel Datum: 2000-01-01Z00:00:00Z)")
    private OffsetDateTime datum;

    @NotNull(message = "Status muss gesetzt werden")
    @NotBlank(message = "Status darf nicht leer sein")
    @Pattern(regexp = "^(neu|storniert|abgeschlossen|versendet|bezahlt)$", message = "Status muss einen der " +
            "folgenden Werte haben: neu, storniert, abgeschlossen, versendet, bezahlt")
    private String status;

    private List<PositionenFuerBestellungDTO> positionen = new ArrayList<>();

    public int getBestellungId() {
        return bestellungId;
    }

    public void setBestellungId(int bestellungId) {
        this.bestellungId = bestellungId;
    }

    public int getKundeId() {
        return kundeId;
    }

    public void setKundeId(int kundeId) {
        this.kundeId = kundeId;
    }

    public int getPersonalNr() {
        return personalNr;
    }

    public void setPersonalNr(int personalNr) {
        this.personalNr = personalNr;
    }

    public OffsetDateTime getDatum() {
        return datum;
    }

    public void setDatum(OffsetDateTime datum) {
        this.datum = datum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PositionenFuerBestellungDTO> getPositionen() {
        return positionen;
    }

    public void setPositionen(List<PositionenFuerBestellungDTO> positionen) {
        this.positionen = positionen;
    }
}
