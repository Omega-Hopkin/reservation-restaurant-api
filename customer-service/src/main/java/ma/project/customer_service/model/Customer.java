package ma.project.customer_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import ma.project.customer_service.StatusCustomer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Column(nullable = false)
    private String prenom;

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,14}$", message = "Numéro de téléphone invalide")
    @NotBlank(message = "Le téléphone est obligatoire")
    @Column(nullable = false)
    private String telephone;

    private String adresse;
    private String ville;

    @Column(columnDefinition = "TEXT")
    private String preferences;

    @Builder.Default
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private Integer nombreReservations = 0;

    @Builder.Default
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private Integer pointsFidelite = 0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private StatusCustomer status = StatusCustomer.ACTIF;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateInscription = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime dernierConnexion = LocalDateTime.now();

    //bach n'stocker les ids dial les réservations
    @ElementCollection
    @CollectionTable(name="restaurant_reservations", joinColumns=@JoinColumn(name="customer_id"))
    @Column(name="reservation_id")
    @Builder.Default
    private List<Long> reservationIds = new ArrayList<>();

    //historique dial les restaurants li visité
    @ElementCollection
    @CollectionTable(name="customer_restaurants_visited", joinColumns=@JoinColumn(name="customer_id"))
    @Column(name="restaurant_id")
    @Builder.Default
    private List<Long> visitedRestaurantIds = new ArrayList<>();

    @PreUpdate
    public void preUpdate() {
        this.dernierConnexion = LocalDateTime.now();
    }

    //méthodes utiles w sf
    public void ajouterReservation(Long reservationId) {
        if (!this.reservationIds.contains(reservationId)) {
            this.reservationIds.add(reservationId);
            this.nombreReservations++;
            this.pointsFidelite += 10; //10 dial no9at par réservation
        }
    }

    public void ajouterRestaurantVisite(Long restaurantId) {
        if (!this.visitedRestaurantIds.contains(restaurantId)) {
            this.visitedRestaurantIds.add(restaurantId);
        }
    }

    public String getNomComplet() {
        return this.prenom + " " + this.nom;
    }
}
