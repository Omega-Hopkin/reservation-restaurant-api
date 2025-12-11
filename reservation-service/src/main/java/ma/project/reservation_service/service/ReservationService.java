package ma.project.reservation_service.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import ma.project.reservation_service.StatusReservation;
import ma.project.reservation_service.client.CustomerFeignClient;
import ma.project.reservation_service.dto.*;
import ma.project.reservation_service.model.Reservation;
import lombok.extern.slf4j.Slf4j;
import ma.project.reservation_service.client.RestaurantFeignClient;
import ma.project.reservation_service.mapping.ReservationMapper;
import ma.project.reservation_service.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
//@AllArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RestaurantFeignClient restaurantFeignClient;
    private final CustomerFeignClient customerFeignClient;
    private final ReservationMapper reservationMapper;

    //créer réservation
    @CircuitBreaker(name = "restaurantService", fallbackMethod = "createReservationFallback")
    public ReservationDto createReservation(CreateReservationRequest request) {
        //vérifier si le restau existe d'abord wla la
        log.info("Création d'une réservation pour le restaurant ID: {}", request.getRestaurantId());

        /*
        //RestaurantDto f'blast "var" la ma ghatch tkhdem
        var restaurant = restaurantFeignClient.getRestaurantById(request.getRestaurantId());

        if(restaurant == null) {
            log.error("Le restaurant avec ID {} n'existe pas ou n'est pas actif.", request.getRestaurantId());
            throw new IllegalArgumentException("Le restaurant spécifié n'existe pas");
        } else if (!restaurant.getActif()) {
            throw new RuntimeException("Le restaurant spécifié n'est pas actif");
        }

        //vérifer la capacité
        verifierCapacite(request.getRestaurantId(), request.getDateReservation(), request.getNombrePersonnes(), restaurant.getCapacite());

        //créer la réservation -
        Reservation reservation = reservationMapper.toEntity(request, restaurant);

        reservation = reservationRepository.save(reservation);
        log.info("Réservation créée avec succès : {}", reservation.getCodeConfirmation());

        return reservationMapper.toDto(reservation);
        */

        CustomerFeignDto customer;
        if (request.getCustomerId() != null) {
            // Client existant
            customer = customerFeignClient.getCustomerById(request.getCustomerId());
            if (customer == null) {
                throw new RuntimeException("Client non trouvé avec l'ID : " + request.getCustomerId());
            }
        } else {
            // Nouveau client ou client existant par email
            Boolean existe = customerFeignClient.customerExists(request.getCustomerEmail());

            if (existe != null && existe) { //f (Boolean.TRUE.equals(existe)) {
                // Le client existe déjà, on le récupère
                customer = customerFeignClient.getCustomerByEmail(request.getCustomerEmail());
            } else {
                // Nouveau client, on le crée
                CreateCustomerRequest customerRequest = reservationMapper.toCreateCustomerRequest(request);

                customer = customerFeignClient.createCustomer(customerRequest);
                log.info("Nouveau client créé avec l'ID : {}", customer.getId());
            }
        }

        // 2. Vérifier la disponibilité du restaurant
        RestaurantFeignDto restaurant = restaurantFeignClient.getRestaurantById(request.getRestaurantId());

        if (restaurant == null || !restaurant.getActif()) {
            throw new RuntimeException("Restaurant non disponible");
        }

        // 3. Vérifier la capacité
        verifierCapacite(request.getRestaurantId(), request.getDateReservation(),
                request.getNombrePersonnes(), restaurant.getCapacite());

        Reservation reservation = reservationMapper.toEntity(request, restaurant, customer);

        reservation = reservationRepository.save(reservation);
        log.info("Réservation créée avec succès : {}", reservation.getCodeConfirmation());

        // 5. Mettre à jour le customer avec la nouvelle réservation
        try {
            customerFeignClient.addReservation(
                    customer.getId(),
                    reservation.getId(),
                    request.getRestaurantId()
            );
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour du customer", e);
            // On ne bloque pas la réservation si ça échoue
        }

        return reservationMapper.toDto(reservation);
    }

    // Fallback si le restaurant-service est indisponible
    public ReservationDto createReservationFallback(CreateReservationRequest request, Exception e) {
        log.error("Impossible de contacter restaurant-service. Création de réservation en mode dégradé", e);

        // En mode dégradé, on crée quand même la réservation mais avec statut spécial
        Reservation reservation = reservationMapper.toEntityFallback(request);

        reservation = reservationRepository.save(reservation);
        return reservationMapper.toDto(reservation);
    }

    // Vérifier la capacité du restaurant
    private void verifierCapacite(Long restaurantId, LocalDate date, Integer nombrePersonnes, Integer capaciteRestaurant) {
        Integer personnesDejaReservees = reservationRepository.compterPersonnesReservees(restaurantId, date);

        if (personnesDejaReservees == null) personnesDejaReservees = 0;


        if (capaciteRestaurant != null &&
                (personnesDejaReservees + nombrePersonnes) > capaciteRestaurant) {
            throw new RuntimeException("Capacité insuffisante pour cette date");
        }
    }

    // Obtenir toutes les réservations
    public List<ReservationDto> obtenirToutesLesReservations() {
        return reservationRepository.findAll().stream()
                .map(reservationMapper::toDto) //.map(this::mapToDto)
                .toList(); //pour java 16+ | sinon dir .collect(Collectors.toList());
    }

    // Obtenir une réservation par ID
    public ReservationDto obtenirReservationParId(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'ID : " + id));
        return reservationMapper.toDto(reservation);
    }

    // Obtenir une réservation par code de confirmation
    public ReservationDto obtenirReservationParCode(String codeConfirmation) {
        Reservation reservation = reservationRepository.findByCodeConfirmation(codeConfirmation)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec le code : " + codeConfirmation));
        return reservationMapper.toDto(reservation);
    }

    // Obtenir les réservations d'un restaurant
    public List<ReservationDto> obtenirReservationsParRestaurant(Long restaurantId) {
        return reservationRepository.findByRestaurantId(restaurantId).stream()
                .map(reservationMapper::toDto)
                .toList();
    }

    // Obtenir les réservations d'un client
    public List<ReservationDto> obtenirReservationsParClient(Long customerId) {
        return reservationRepository.findByCustomerId(customerId).stream()
                .map(reservationMapper::toDto)
                .toList();
    }

    // Obtenir les réservations d'un restaurant pour une date donnée
    public List<ReservationDto> obtenirReservationsParRestaurantEtDate(Long restaurantId, LocalDate date) {
        return reservationRepository.findByRestaurantIdAndDateReservation(restaurantId, date).stream()
                .map(reservationMapper::toDto)
                .toList();
    }

    // Confirmer une réservation
    public ReservationDto confirmerReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        if (reservation.getStatut() != StatusReservation.EN_ATTENTE) {
            throw new RuntimeException("Seules les réservations en attente peuvent être confirmées");
        }

        reservation.setStatut(StatusReservation.CONFIRMEE);
        reservation = reservationRepository.save(reservation);
        log.info("Réservation confirmée : {}", reservation.getCodeConfirmation());

        return reservationMapper.toDto(reservation);
    }

    // Annuler une réservation
    public ReservationDto annulerReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        if (reservation.getStatut() == StatusReservation.ANNULEE ||
                reservation.getStatut() == StatusReservation.TERMINEE) {
            throw new RuntimeException("Cette réservation ne peut pas être annulée");
        }

        reservation.setStatut(StatusReservation.ANNULEE);
        reservation = reservationRepository.save(reservation);
        log.info("Réservation annulée : {}", reservation.getCodeConfirmation());

        return reservationMapper.toDto(reservation);
    }

    // Marquer une réservation comme terminée
    public ReservationDto terminerReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        if (reservation.getStatut() != StatusReservation.CONFIRMEE) {
            throw new RuntimeException("Seules les réservations confirmées peuvent être terminées");
        }

        reservation.setStatut(StatusReservation.TERMINEE);
        reservation = reservationRepository.save(reservation);
        log.info("Réservation terminée : {}", reservation.getCodeConfirmation());

        return reservationMapper.toDto(reservation);
    }

    // Marquer comme non présenté
    public ReservationDto marquerNonPresentee(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        reservation.setStatut(StatusReservation.NON_PRESENTEE);
        reservation = reservationRepository.save(reservation);
        log.info("Réservation marquée comme non présentée : {}", reservation.getCodeConfirmation());

        return reservationMapper.toDto(reservation);
    }

    // Mettre à jour une réservation
    public ReservationDto mettreAJourReservation(Long id, CreateReservationRequest request) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        if (reservation.getStatut() == StatusReservation.TERMINEE ||
                reservation.getStatut() == StatusReservation.ANNULEE) {
            throw new RuntimeException("Cette réservation ne peut pas être modifiée");
        }

        // Mise à jour des champs
        reservationMapper.updateEntityFromRequest(reservation, request);

        reservation = reservationRepository.save(reservation);
        log.info("Réservation mise à jour : {}", reservation.getCodeConfirmation());

        return reservationMapper.toDto(reservation);
    }

    // Statistiques pour un restaurant
    public StatistiquesReservationDto getStatisticsRestaurant(Long restaurantId) {
        List<Reservation> reservations = reservationRepository.findByRestaurantId(restaurantId);

        long total = reservations.size();
        long enAttente = reservations.stream()
                .filter(r -> r.getStatut() == StatusReservation.EN_ATTENTE)
                .count();
        long confirmees = reservations.stream()
                .filter(r -> r.getStatut() == StatusReservation.CONFIRMEE)
                .count();
        long annulees = reservations.stream()
                .filter(r -> r.getStatut() == StatusReservation.ANNULEE)
                .count();
        long terminees = reservations.stream()
                .filter(r -> r.getStatut() == StatusReservation.TERMINEE)
                .count();
        long nonPresentees = reservations.stream()
                .filter(r -> r.getStatut() == StatusReservation.NON_PRESENTEE)
                .count();

        return reservationMapper.toStatistiquesDto(restaurantId, total, enAttente, confirmees, annulees, terminees, nonPresentees);
    }
}
