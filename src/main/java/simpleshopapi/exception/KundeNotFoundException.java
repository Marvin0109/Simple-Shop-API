package simpleshopapi.exception;

public class KundeNotFoundException extends RuntimeException {
    public KundeNotFoundException(Integer id) {
        super("Kunde mit ID " + id + " nicht gefunden");
    }

    public KundeNotFoundException(String email) {
        super("Kunde mit " + email + " nicht gefunden");
    }
}
