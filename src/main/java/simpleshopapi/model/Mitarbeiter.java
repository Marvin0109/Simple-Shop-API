package simpleshopapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Mitarbeiter {

    private Integer personalNr;

    @NotNull(message = "Passwort darf nicht null sein")
    @NotBlank(message = "Passwort darf nicht leer sein")
    @Size(min = 5, max = 20, message = "Passwort muss zwischen 5 und 20 Zeichen lang sein")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$", message = "Passwort muss mind. ein Buchstabe" +
            ", eine Zahl und ein Sonderzeichen enthalten")
    private String passwort;

    @NotNull(message = "Email darf nicht null sein")
    @NotBlank(message = "Email darf nicht leer sein")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Ungültige Email Adresse")
    private String email;

    @NotNull(message = "Vorname darf nicht null sein")
    @NotBlank(message = "Vorname darf nicht leer sein")
    private String vorname;

    @NotNull(message = "Nachname darf nicht null sein")
    @NotBlank(message = "Nachname darf nicht leer sein")
    private String nachname;

    public Integer getPersonalNr() {
        return personalNr;
    }

    public void setPersonalNr(Integer personalNr) {
        this.personalNr = personalNr;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
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
}
