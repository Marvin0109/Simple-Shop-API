package simpleshopapi.exception;

public class AdresseNotFoundException extends RuntimeException {
    public AdresseNotFoundException(Integer id) {
        super("Adresse mit der ID " + id + " wurde nicht gefunden");
    }
}
