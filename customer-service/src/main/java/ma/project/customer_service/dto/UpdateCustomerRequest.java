package ma.project.customer_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UpdateCustomerRequest {
    private String nom;
    private String prenom;

    @Email(message = "Email invalide")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,14}$", message = "Numéro de téléphone invalide")
    private String telephone;

    private String adresse;
    private String ville;
    private String codePostal;
    private String preferences;
}
