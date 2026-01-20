package simpleshopapi.dto;

import jakarta.validation.constraints.*;

public class MitarbeiterLoginDTO {

    @NotNull(message = "Personal Nr. muss gesetzt werden")
    @Min(value = 1, message = "Mitarbeiter ID fangen bei 1 an")
    private int personalNr;

    @NotNull(message = "Passwort darf nicht null sein")
    @NotBlank(message = "Passwort darf nicht leer sein")
    @Size(min = 5, max = 20, message = "Passwort muss zwischen 5 und 20 Zeichen lang sein")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$", message = "Passwort muss mind. ein Buchstabe" +
            ", eine Zahl und ein Sonderzeichen enthalten")
    private String passwort;

    public int getPersonalNr() {
        return personalNr;
    }
    public void setPersonalNr(int personalNr) {
        this.personalNr = personalNr;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }
}
