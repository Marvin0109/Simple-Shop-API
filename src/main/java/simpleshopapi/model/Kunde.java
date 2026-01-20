package simpleshopapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import simpleshopapi.dto.AdresseMitTypDTO;

import java.util.ArrayList;
import java.util.List;

public class Kunde {

    private Integer kundeId;

    @NotNull(message = "Email muss gesetzt werden")
    @NotBlank(message = "Email darf nicht leer sein")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Ungültige Email Adresse")
    private String email;

    @NotNull(message = "Vorname muss gesetzt werden")
    @NotBlank(message = "Vorname darf nicht leer sein")
    @Pattern(regexp = "^[A-ZÄÖÜ][a-zäöüß]*$", message = "Vornamen bestehen nur aus Buchstaben des " +
            "lat. Alphabets inkl. Umlaute")
    private String vorname;

    @NotNull(message = "Nachname muss gesetzt werden")
    @NotBlank(message = "Nachname darf nicht leer sein")
    @Pattern(regexp = "^[A-ZÄÖÜ][a-zäöüß]*$", message = "Nachnamen bestehen nur aus Buchstaben des " +
            "lat. Alphabets inkl. Umlaute")
    private String nachname;

    @NotNull(message = "Passwort darf nicht null sein")
    @NotBlank(message = "Passwort darf nicht leer sein")
    @Size(min = 5, max = 20, message = "Passwort muss zwischen 5 und 20 Zeichen lang sein")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$", message = "Passwort muss mind. ein Buchstabe" +
            ", eine Zahl und ein Sonderzeichen enthalten")
    private String passwort;

    private List<AdresseMitTypDTO> adressenMitTyp = new ArrayList<>();

    public Integer getKundeId() {
        return kundeId;
    }

    public void setKundeId(Integer kundeId) {
        this.kundeId = kundeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public List<AdresseMitTypDTO> getAdressen() {
        return adressenMitTyp;
    }

    public void setAdresse(List<AdresseMitTypDTO> adressen) {
        this.adressenMitTyp = adressen;
    }
}
