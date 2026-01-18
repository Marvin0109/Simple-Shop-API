package simpleshopapi.exception;

public class MitarbeiterNotFoundException extends RuntimeException {

    public MitarbeiterNotFoundException(int id) {
        super("Mitarbeiter mit ID " + id + " nicht gefunden");
    }
}
