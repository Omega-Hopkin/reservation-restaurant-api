package ma.project.reservation_service.client;

import lombok.extern.slf4j.Slf4j;
import ma.project.reservation_service.dto.CustomerFeignDto;
import ma.project.reservation_service.dto.CreateCustomerRequest;
import ma.project.reservation_service.utils.CustomerSafeFactory;
import org.springframework.stereotype.Component;
//import static ma.project.reservation_service.utils.CustomerSafeFactory.safeCustomer;

@Component
@Slf4j
public class CustomerFeignClientFallback implements CustomerFeignClient {
    @Override
    public CustomerFeignDto getCustomerById(Long id) {
        log.error("Fallback activé pour le customer ID: {}", id);
        return CustomerSafeFactory.safeCustomer(id, null);
    }

    @Override
    public CustomerFeignDto getCustomerByEmail(String email) {
        log.error("Fallback activé pour le customer email: {}", email);
        return CustomerSafeFactory.safeCustomer(null, email);
    }

    @Override
    public CustomerFeignDto createCustomer(CreateCustomerRequest request) {
        log.error("Fallback activé pour la création du customer: {}", request.getEmail());
        return CustomerSafeFactory.safeCustomer(null, request.getEmail());
    }

    @Override
    public CustomerFeignDto addReservation(Long customerId, Long reservationId, Long restaurantId) {
        log.error("Fallback activé pour l'ajout de réservation");
        return CustomerSafeFactory.safeCustomer(customerId, null);
    }

    @Override
    public Boolean customerExists(String email) {
        log.error("Fallback activé pour la vérification de l'existence du customer");
        return false;
    }
}
