package ma.project.customer_service.dto;

import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CustomerStatsDto {
    private Long customerId;
    private String nomComplet;
    private Integer nombreReservations;
    private Integer pointsFidelite;
    private Integer nombreRestaurantsVisites;
    private String restaurantPrefere;
}