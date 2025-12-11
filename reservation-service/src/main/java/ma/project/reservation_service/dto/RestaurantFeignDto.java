package ma.project.reservation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RestaurantFeignDto {
    private Long id;
    private String nom;
    private String adresse;
    private String ville;
    private String telephone;
    private String email;
    private Integer capacite;
    private Boolean actif;
}