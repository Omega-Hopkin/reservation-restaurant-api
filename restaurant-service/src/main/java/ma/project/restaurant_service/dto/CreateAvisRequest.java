package ma.project.restaurant_service.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class CreateAvisRequest {
    @NotBlank(message="Le nom de l'utilsateur est obligatoire")
    private String nomUtilisateur;

    @NotBlank(message="La note est obligatoire")
    @Min(value = 1, message = "La note minimale est 1")
    @Max(value = 5, message = "La note maximale est 5")
    private Integer note;

    @Size(max = 800, message = "Le commentaire ne doit pas dépasser 800 caractères")
    private String commentaire;
}
