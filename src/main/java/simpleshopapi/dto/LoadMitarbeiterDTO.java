package simpleshopapi.dto;

public record LoadMitarbeiterDTO (
        Integer personalNr,
        String email,
        String vorname,
        String nachname) {
}
