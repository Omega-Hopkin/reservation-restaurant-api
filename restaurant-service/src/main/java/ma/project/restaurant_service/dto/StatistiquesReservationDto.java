package ma.project.restaurant_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class StatistiquesReservationDto {
    private Long restaurantId;
    private Long totalReservations;
    private Long enAttente;
    private Long confirmees;
    private Long annulees;
    private Long terminees;
    private Long nonPresentees;
}
