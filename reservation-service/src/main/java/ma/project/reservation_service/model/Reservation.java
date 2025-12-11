package ma.project.reservation_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import ma.project.reservation_service.StatusReservation;

@Entity
@Table(name = "reservations")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "L'ID du restaurant est obligatoire")
    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    // Informations stockées localement depuis restaurant-service
    @Column(name = "restaurant_nom")
    private String restaurantNom;

    @Column(name = "restaurant_adresse")
    private String restaurantAdresse;

    @Column(name = "restaurant_telephone")
    private String restaurantTelephone;

    // Informations client
    @NotNull(message = "L'ID du client est obligatoire")
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "customer_nom")
    private String customerNom;

    @Column(name = "customer_prenom")
    private String customerPrenom;

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    @Column(name = "customer_email", nullable = false)
    private String customerEmail;

    @Pattern(regexp = "^\\+?[0-9]{10,14}$", message = "Numéro de téléphone invalide")
    @NotBlank(message = "Le téléphone est obligatoire")
    @Column(nullable = false, name = "customer_telephone")
    private String customerTelephone;

    // Détails de la réservation
    @NotNull(message = "La date de réservation est obligatoire")
    @Column(nullable = false)
    private LocalDate dateReservation;

    @NotNull(message = "L'heure de réservation est obligatoire")
    @Column(nullable = false)
    private LocalTime heureReservation;

    @NotNull(message = "Le nombre de personnes est obligatoire")
    @Min(value = 1, message = "Le nombre de personnes doit être au moins 1")
    @Max(value = 20, message = "Le nombre de personnes ne peut pas dépasser 20")
    @Column(nullable = false)
    private Integer nombrePersonnes;

    @Column(columnDefinition = "TEXT")
    private String commentaires;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusReservation statut = StatusReservation.EN_ATTENTE;

    @Column(name = "code_confirmation", unique = true)
    private String codeConfirmation;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime dateModification = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.dateModification = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.codeConfirmation == null) {
            this.codeConfirmation = genererCodeConfirmation();
        }
    }

    private String genererCodeConfirmation() {
        return "RES-" + System.currentTimeMillis() + "-" +
                (int)(Math.random() * 10000);
    }
}
