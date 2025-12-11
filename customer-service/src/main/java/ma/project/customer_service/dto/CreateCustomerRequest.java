package ma.project.customer_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CreateCustomerRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,14}$", message = "Numéro de téléphone invalide")
    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    private String adresse;
    private String ville;
    private String codePostal;
    private String preferences;
}
