package ma.project.restaurant_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AvisDto {
    private Long id;

    @NotNull
    private Long restaurantId;

    @NotBlank(message="Le nom de l'utilsateur est obligatoire")
    private String nomUtilisateur;

    @NotNull
    @Min(1) @Max(5)
    private Integer note;

    private String commentaire;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
}
