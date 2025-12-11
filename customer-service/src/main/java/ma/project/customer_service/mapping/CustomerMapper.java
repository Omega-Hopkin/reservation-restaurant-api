package ma.project.customer_service.mapping;

import ma.project.customer_service.dto.CustomerDto;
import ma.project.customer_service.dto.CustomerStatsDto;
import ma.project.customer_service.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public static CustomerDto toDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .nom(customer.getNom())
                .prenom(customer.getPrenom())
                .email(customer.getEmail())
                .telephone(customer.getTelephone())
                .adresse(customer.getAdresse())
                .ville(customer.getVille())
                .preferences(customer.getPreferences())
                .nombreReservations(customer.getNombreReservations())
                .pointsFidelite(customer.getPointsFidelite())
                .statut(customer.getStatus())
                .dateInscription(customer.getDateInscription())
                .derniereConnexion(customer.getDernierConnexion())
                .reservationIds(customer.getReservationIds())
                .restaurantsVisites(customer.getVisitedRestaurantIds())
                .build();
    }

    public static CustomerStatsDto toStatsDto(Customer customer) {
        return CustomerStatsDto.builder()
                .customerId(customer.getId())
                .nomComplet(customer.getNomComplet())
                .nombreReservations(customer.getNombreReservations())
                .pointsFidelite(customer.getPointsFidelite())
                .nombreRestaurantsVisites(customer.getVisitedRestaurantIds().size())
                .build();
    }
}
