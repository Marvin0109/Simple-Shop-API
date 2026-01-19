package simpleshopapi.dto;

import simpleshopapi.model.Adresse;

public class AdresseMitTypDTO {

    private Adresse adresse;
    private String typ;

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }
}
