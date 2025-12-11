package ma.project.reservation_service.dto;

import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CustomerFeignDto {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String ville;
    private String preferences;
    private Integer nombreReservations;
    private Integer pointsFidelite;
}