package simpleshopapi.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class KundenAdresse {

    @NotNull(message = "Kunden ID darf nicht null sein")
    @Min(value = 1, message = "Kunden ID muss größer als 0 sein")
    private Integer kundenId;

    @NotNull(message = "Adresse ID darf nicht null sein")
    @Min(value = 1, message = "Adresse ID muss größer als 0 sein")
    private Integer adresseId;

    @NotNull(message = "Typ der Adresse darf nicht null sein")
    @NotBlank(message = "Typ der Adresse darf nicht leer sein")
    @Pattern(regexp = "^(Lieferadresse|Rechnungsadresse)$", message = "Typ der Adresse muss einen der " +
    "folgenden Werte haben: Lieferadresse oder Rechnungsadresse")
    private String typ;

    public Integer getKundenId() {
        return kundenId;
    }

    public void setKundenId(Integer kundenId) {
        this.kundenId = kundenId;
    }

    public Integer getAdresseId() {
        return adresseId;
    }

    public void setAdresseId(Integer adresseId) {
        this.adresseId = adresseId;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }
}
