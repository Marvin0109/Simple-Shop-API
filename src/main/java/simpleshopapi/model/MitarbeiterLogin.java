package simpleshopapi.model;

public class MitarbeiterLogin {

    private int personalNr;
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
