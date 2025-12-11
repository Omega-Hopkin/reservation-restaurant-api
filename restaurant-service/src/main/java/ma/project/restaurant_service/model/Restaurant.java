package ma.project.restaurant_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="restaurants")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Restaurant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="Le nom du restaurant est obligatoire")
    private String nom;

    @NotBlank(message="L'adresse du restaurant est obligatoire")
    @Column(nullable=false)
    private String adresse;

    @NotBlank(message="La ville est obligatoire")
    private String ville;

    @Column(nullable=false)
    private Double latitude; //Double 7it t9dr tkoun null

    @Column(nullable=false)
    private Double longitude; //Double 7it t9dr tkoun null

    @Pattern(regexp = "^\\+?[1-9]\\d{7,14}$", message="Format de téléphone invalide")
    private String telephone;

    @Email(message="Format d'email invalide")
    private String email;

    @Column(columnDefinition="TEXT")
    private String description;

    @NotBlank(message="Le type de cuisine est obligatoire")
    private String typeCuisine;

    @Min(value=0, message="Le prix moyen ne doit pas être négatif")
    @Max(value=5, message="Le prix moyen doit être entre 0 et 5")
    private Integer prixMoyen;

    @Min(value=1, message="La capacité doit être au moins de 1")
    private Integer capacite;

    private LocalTime heureOuverture;
    private LocalTime heureFermeture;

    @Column(name="google_place_id", unique = true)
    private String googlePlaceId;

    @Min(value=0)
    @Max(value=5)
    @Column(columnDefinition="DECIMAL(2,1) DEFAULT 0")
    private Double noteGlobale=0.0;

    @Column(columnDefinition="INTEGER DEFAULT 0")
    private Integer nombreAvis=0;

    @Builder.Default
    @Column(columnDefinition="BOOLEAN DEFAULT true")
    private Boolean actif=true;

    @ElementCollection
    @CollectionTable(name="restaurant_images", joinColumns=@JoinColumn(name="restaurant_id"))
    @Column(name="image_url")
    @Builder.Default
    private List<String> imagesUrls = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name="restaurant_jours_fermeture", joinColumns=@JoinColumn(name="restaurant_id"))
    @Column(name="jour")
    @Builder.Default
    private List<String> joursFermeture = new ArrayList<>();

    @OneToMany(mappedBy="restaurant", cascade=CascadeType.ALL, orphanRemoval=true)
    @Builder.Default
    private List<Avis> avis = new ArrayList<>();

    public void ajouterAvis(Avis avisItem) {
        avis.add(avisItem);
        avisItem.setRestaurant(this);
        calculerNoteGlobale();
    }

    public void calculerNoteGlobale() {
        if(avis.isEmpty()) {
            this.noteGlobale = 0.0;
            this.nombreAvis = 0;
            //return;
        } else {
            this.noteGlobale = avis.stream()
                    .mapToDouble(Avis::getNote)
                    .average()
                    .orElse(0.0);
            this.nombreAvis = avis.size();
        }
    }
}
