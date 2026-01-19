package simpleshopapi.model;

import simpleshopapi.dto.PositionenFuerBestellungDTO;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class Bestellung {

    private int bestellungId;
    private int kundeId;
    private int personalNr;
    private OffsetDateTime datum;
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
