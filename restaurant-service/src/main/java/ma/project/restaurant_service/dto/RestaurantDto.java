package ma.project.restaurant_service.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RestaurantDto {
    private Long id;

    @NotBlank(message="Le nom du restaurant est obligatoire")
    private String nom;

    @NotBlank(message="L'adresse du restaurant est obligatoire")
    private String adresse;

    @NotBlank(message="La ville est obligatoire")
    private String ville;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private String telephone;
    private String email;
    private String description;

    @NotBlank(message="Le type de cuisine est obligatoire")
    private String typeCuisine;

    @Min(1) @Max(5)
    private Integer prixMoyen;

    @Min(1)
    private Integer capacite;

    private LocalDateTime heureOuverture;
    private LocalDateTime heureFermeture;

    private String googlePlaceId;
    private Double noteGlobale;
    private Integer nombreAvis;
    private Boolean actif;

    private List<String> imagesUrls;
    private List<String> joursFermeture;

    private Double distance;
}
