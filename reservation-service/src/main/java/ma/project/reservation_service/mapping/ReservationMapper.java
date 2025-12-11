package ma.project.reservation_service.mapping;

import ma.project.reservation_service.StatusReservation;
import ma.project.reservation_service.dto.*;
import ma.project.reservation_service.model.Reservation;
//import ma.project.reservation_service.StatusReservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    public Reservation toEntity(CreateReservationRequest request, RestaurantFeignDto restaurant, CustomerFeignDto customer) {
        if (request == null) {
            throw new IllegalArgumentException("CreateReservationRequest ne peut pas être null");
        }
        if (restaurant == null) {
            throw new IllegalArgumentException("RestaurantDto ne peut pas être null en mode normal");
        }
        if (customer == null) {
            throw new IllegalArgumentException("CustomerDto ne peut pas être null en mode normal");
        }

        return Reservation.builder()
                .restaurantId(request.getRestaurantId())
                .restaurantNom(restaurant.getNom())
                .restaurantAdresse(restaurant.getAdresse())
                .restaurantTelephone(restaurant.getTelephone())
                .customerId(request.getCustomerId())
                .customerNom(request.getCustomerNom())
                .customerPrenom(request.getCustomerPrenom())
                .customerEmail(request.getCustomerEmail())
                .customerTelephone(request.getCustomerTelephone())
                .dateReservation(request.getDateReservation())
                .heureReservation(request.getHeureReservation())
                .nombrePersonnes(request.getNombrePersonnes())
                .commentaires(request.getCommentaires())
                .statut(StatusReservation.EN_ATTENTE)
                .build();
    }

    /*public Reservation toEntity(CreateReservationRequest request, RestaurantFeignDto restaurantDto) {
        return toEntity(request, restaurantDto, StatusReservation.EN_ATTENTE);
    }*/

    public ReservationDto toDto(Reservation restaurant) {
    //public RestaurantDto toDto(Restaurant restaurant) {
        return ReservationDto.builder()
                .id(restaurant.getId())
                .restaurantId(restaurant.getRestaurantId())
                .restaurantNom(restaurant.getRestaurantNom())
                .restaurantAdresse(restaurant.getRestaurantAdresse())
                .restaurantTelephone(restaurant.getRestaurantTelephone())
                .customerNom(restaurant.getCustomerNom())
                .customerPrenom(restaurant.getCustomerPrenom())
                .customerEmail(restaurant.getCustomerEmail())
                .customerTelephone(restaurant.getCustomerTelephone())
                .dateReservation(restaurant.getDateReservation())
                .heureReservation(restaurant.getHeureReservation())
                .nombrePersonnes(restaurant.getNombrePersonnes())
                .commentaires(restaurant.getCommentaires())
                .statut(restaurant.getStatut())
                .codeConfirmation(restaurant.getCodeConfirmation())
                .dateCreation(restaurant.getDateCreation())
                .dateModification(restaurant.getDateModification())
                .build();
    }

    public Reservation toEntityFallback(CreateReservationRequest request) {
        return Reservation.builder()
                .restaurantId(request.getRestaurantId())
                .restaurantNom("À confirmer")
                .customerId(request.getCustomerId() != null ? request.getCustomerId() : 0L)
                .customerNom(request.getCustomerNom())
                .customerPrenom(request.getCustomerPrenom())
                .customerEmail(request.getCustomerEmail())
                .customerTelephone(request.getCustomerTelephone())
                .dateReservation(request.getDateReservation())
                .heureReservation(request.getHeureReservation())
                .nombrePersonnes(request.getNombrePersonnes())
                .commentaires(request.getCommentaires())
                .statut(StatusReservation.EN_ATTENTE)
                .build();
    }

    public CreateCustomerRequest toCreateCustomerRequest(CreateReservationRequest request) {
        return CreateCustomerRequest.builder()
                .nom(request.getCustomerNom())
                .prenom(request.getCustomerPrenom())
                .email(request.getCustomerEmail())
                .telephone(request.getCustomerTelephone())
                .adresse(request.getCustomerAdresse())
                .ville(request.getCustomerVille())
                .codePostal(request.getCustomerCodePostal())
                .build();
    }


    public void updateEntityFromRequest(Reservation reservation, CreateReservationRequest request) {
        reservation.setCustomerNom(request.getCustomerNom());
        reservation.setCustomerPrenom(request.getCustomerPrenom());
        reservation.setCustomerEmail(request.getCustomerEmail());
        reservation.setCustomerTelephone(request.getCustomerTelephone());
        reservation.setDateReservation(request.getDateReservation());
        reservation.setHeureReservation(request.getHeureReservation());
        reservation.setNombrePersonnes(request.getNombrePersonnes());
        reservation.setCommentaires(request.getCommentaires());
    }

    public StatistiquesReservationDto toStatistiquesDto(Long restaurantId, long total, long enAttente, long confirmees, long annulees, long terminees, long nonPresentees) {
        return StatistiquesReservationDto.builder()
                .restaurantId(restaurantId)
                .totalReservations(total)
                .enAttente(enAttente)
                .confirmees(confirmees)
                .annulees(annulees)
                .terminees(terminees)
                .nonPresentees(nonPresentees)
                .build();
    }
}