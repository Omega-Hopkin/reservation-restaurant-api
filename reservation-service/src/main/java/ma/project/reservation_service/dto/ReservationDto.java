package ma.project.reservation_service.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.project.reservation_service.StatusReservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor  @AllArgsConstructor
@Builder
public class ReservationDto {
    private Long id;

    @NotNull(message = "L'ID du restaurant est obligatoire")
    private Long restaurantId;

    private String restaurantNom;
    private String restaurantAdresse;
    private String restaurantTelephone;

    @NotNull(message = "L'ID du client est obligatoire")
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

    // Détails de la réservation
    @NotNull(message = "La date de réservation est obligatoire")
    @FutureOrPresent(message = "La date de réservation doit être aujourd'hui ou dans le futur")
    private LocalDate dateReservation;

    @NotNull(message = "L'heure de réservation est obligatoire")
    private LocalTime heureReservation;

    @NotNull(message = "Le nombre de personnes est obligatoire")
    @Min(value = 1, message = "Le nombre de personnes doit être au moins 1")
    @Max(value = 20, message = "Le nombre de personnes ne peut pas dépasser 20")
    private Integer nombrePersonnes;

    private String commentaires;
    private StatusReservation statut;
    private String codeConfirmation;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
}