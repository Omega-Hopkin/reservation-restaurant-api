package ma.project.customer_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.project.customer_service.StatusCustomer;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CustomerDto {
    private Long id;

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
    private String preferences;
    private Integer nombreReservations;
    private Integer pointsFidelite;
    private StatusCustomer statut;
    private LocalDateTime dateInscription;
    private LocalDateTime derniereConnexion;
    private List<Long> reservationIds;
    private List<Long> restaurantsVisites;
}