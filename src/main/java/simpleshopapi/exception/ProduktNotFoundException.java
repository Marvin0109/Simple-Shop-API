package simpleshopapi.exception;

public class ProduktNotFoundException extends RuntimeException {
    public ProduktNotFoundException(String sku) {
        super("Produkt mit SKU " + sku + " nicht gefunden");
    }
}
