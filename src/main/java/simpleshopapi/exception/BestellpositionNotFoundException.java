package simpleshopapi.exception;

public class BestellpositionNotFoundException extends RuntimeException {
    public BestellpositionNotFoundException(Integer id) {
        super("Die Bestellung mit der ID " + " wurde nicht gefunden");
    }
}
