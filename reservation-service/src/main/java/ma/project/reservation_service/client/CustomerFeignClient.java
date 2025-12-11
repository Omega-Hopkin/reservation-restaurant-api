package ma.project.reservation_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import ma.project.reservation_service.dto.CustomerFeignDto;
import ma.project.reservation_service.dto.CreateCustomerRequest;
import org.springframework.web.bind.annotation.*;

//url = "http://localhost:8083/api/customers" <- idan retiré (géré par discovery)
//bla 'url' (discovery gère ça), bla 'path' (mappings méthodes suffisants)
@FeignClient(name = "customer-service", fallback = RestaurantFeignClientFallback.class)
public interface CustomerFeignClient {
    @GetMapping("/customers/{id}")
    CustomerFeignDto getCustomerById(@PathVariable("id") Long id);

    @GetMapping("/customers/email/{email}")
    CustomerFeignDto getCustomerByEmail(@PathVariable("email") String email);

    @PostMapping("/customers")
    CustomerFeignDto createCustomer(@RequestBody CreateCustomerRequest request);

    @PostMapping("/customers/{customerId}/reservations/{reservationId}/restaurant/{restaurantId}")
    CustomerFeignDto addReservation(
            @PathVariable("customerId") Long customerId,
            @PathVariable("reservationId") Long reservationId,
            @PathVariable("restaurantId") Long restaurantId
    );

    @GetMapping("/customers/existe/{email}")
    Boolean customerExists(@PathVariable("email") String email);
}
