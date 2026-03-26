package simpleshopapi.dto;

public record LoadKundeDTO (
        Integer kundeId,
        String email,
        String vorname,
        String nachname) {
}
