package ma.project.reservation_service.client;

import ma.project.reservation_service.dto.RestaurantFeignDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "restaurant-service", url = "http://localhost:8081/api/restaurants", fallback = RestaurantFeignClientFallback.class)
public interface RestaurantFeignClient {
    @GetMapping("/restaurants/{id}")
    RestaurantFeignDto getRestaurantById(@PathVariable Long id);
}
