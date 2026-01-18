package simpleshopapi.exception;

public class BestellungNotFoundException extends RuntimeException {
    public BestellungNotFoundException(Integer id) {
        super("Die Bestellung mit der ID " + id + " wurde nicht gefunden");
    }
}
