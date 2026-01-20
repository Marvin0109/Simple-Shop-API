package simpleshopapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Adresse {

    private int adresseId;

    @NotNull(message = "Status muss gesetzt werden")
    private Boolean aktiv;

    @NotNull(message = "Strasse muss gesetzt werden")
    @NotBlank(message = "Strasse darf nicht leer sein")
    @Size(max = 60, message = "Strasse darf bis 60 Zeichen lang sein")
    @Pattern(regexp = "^[A-ZÄÖÜ][a-zäöüß]*$", message = "Strasse besteht nur aus Buchstaben des lat. Alphabet inkl." +
            "Umlaute, angefangen mit einem Großbuchstaben")
    private String strasse;

    @NotNull(message = "Hausnummer muss gesetzt werden")
    @NotBlank(message = "Hausnummer darf nicht leer sein")
    @Pattern(regexp = "^[0-9]+[a-z]?$", message = "Hausnummer besteht nur aus Zahlen, kann aber mit einem einzelnen" +
            "Kleinbuchstaben abschließen")
    private String hausnummer;

    @NotNull(message = "PLZ muss gesetzt werden")
    @NotBlank(message = "PLZ darf nicht leer sein")
    @Pattern(regexp = "^[0-9]{1,12}$", message = "PLZ sollte eine Zahl zwischen 1 und 12 Zeichen lang sein")
    private String plz;

    @NotNull(message = "Ort muss gesetzt werden")
    @NotBlank(message = "Ort darf nicht leer sein")
    private String ort;

    @NotNull(message = "Land muss gesetzt werden")
    @NotBlank(message = "Land darf nicht leer sein")
    private String land;

    public int getAdresseId() {
        return adresseId;
    }

    public void setAdresseId(int adresseId) {
        this.adresseId = adresseId;
    }

    public Boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }
}
