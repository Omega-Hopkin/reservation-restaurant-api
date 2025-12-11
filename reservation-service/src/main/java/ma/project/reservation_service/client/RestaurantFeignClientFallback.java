package ma.project.reservation_service.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ma.project.reservation_service.dto.RestaurantFeignDto;

@Component
@Slf4j
public class RestaurantFeignClientFallback implements RestaurantFeignClient {
    @Override
    public RestaurantFeignDto getRestaurantById(Long id) {
        log.error("Fallback activated for getRestaurantById with id: {}", id);
        return RestaurantFeignDto.builder()
                // l'restau li par défaut wla null...
                .id(id)
                .nom("Restaurant non disponible")
                .adresse("Adresse non disponible")
                .telephone("N/A")
                .actif(false)
                .build();
    }
}
