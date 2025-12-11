package ma.project.reservation_service.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CreateReservationRequest {
    @NotNull(message = "L'ID du restaurant est obligatoire")
    private Long restaurantId;

    private Long customerId;

    @NotBlank(message = "Le nom du client est obligatoire")
    @Column(nullable = false)
    private String customerNom;

    @NotBlank(message = "Le prénom du client est obligatoire")
    @Column(nullable = false)
    private String customerPrenom;

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String customerEmail;

    @Pattern(regexp = "^\\+?[0-9]{10,14}$", message = "Numéro de téléphone invalide")
    @NotBlank(message = "Le téléphone est obligatoire")
    private String customerTelephone;

    private String customerAdresse;
    private String customerVille;
    private String customerCodePostal;

    // Détails de la réservation
    @NotNull(message = "La date de réservation est obligatoire")
    private LocalDate dateReservation;

    @NotNull(message = "L'heure de réservation est obligatoire")
    private LocalTime heureReservation;

    @NotNull(message = "Le nombre de personnes est obligatoire")
    @Min(value = 1, message = "Le nombre de personnes doit être au moins 1")
    @Max(value = 20, message = "Le nombre de personnes ne peut pas dépasser 20")
    private Integer nombrePersonnes;

    //optionelle hada
    private String commentaires;
}
