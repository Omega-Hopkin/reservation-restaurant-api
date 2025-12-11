package ma.project.reservation_service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.project.reservation_service.dto.CreateReservationRequest;
import ma.project.reservation_service.dto.ReservationDto;
import ma.project.reservation_service.dto.StatistiquesReservationDto;
import ma.project.reservation_service.service.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Slf4j
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "*")
public class ReservationController {
    private final ReservationService reservationService;

    //créer une réservation
    @PostMapping
    public ResponseEntity<ReservationDto> createReservation(@Valid @RequestBody CreateReservationRequest request) {
        log.info("Création d'une réservation pour le restaurant ID : {}", request.getRestaurantId());

        ReservationDto reservationDto = reservationService.createReservation(request);
        return ResponseEntity.status(201).body(reservationDto);
        // return ResponseEntity.status(HttpStatus.CREATED).body(reservationDto);
    }

    //obtenir toutes les réservations
    @GetMapping
    public ResponseEntity<List<ReservationDto>> getAllReservations() {
        log.info("Récupération de toutes les réservations");

        List<ReservationDto> reservationDto = reservationService.obtenirToutesLesReservations();

        return ResponseEntity.status(200).body(reservationDto);
        //return ResponseEntity.ok(reservations);
    }

    //obtenir une réservationpar ID
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> obtenirReservationParId(@PathVariable Long id) {
        log.info("Récupération de la réservation ID : {}", id);
        ReservationDto reservation = reservationService.obtenirReservationParId(id);
        return ResponseEntity.ok(reservation);
    }

    // Obtenir une réservation par code de confirmation
    @GetMapping("/code/{codeConfirmation}")
    public ResponseEntity<ReservationDto> obtenirReservationParCode(
            @PathVariable String codeConfirmation) {
        log.info("Récupération de la réservation avec le code : {}", codeConfirmation);
        ReservationDto reservation = reservationService.obtenirReservationParCode(codeConfirmation);
        return ResponseEntity.ok(reservation);
    }

    // Obtenir les réservations d'un restaurant
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<ReservationDto>> obtenirReservationsParRestaurant(
            @PathVariable Long restaurantId) {
        log.info("Récupération des réservations du restaurant : {}", restaurantId);
        List<ReservationDto> reservations = reservationService.obtenirReservationsParRestaurant(restaurantId);
        return ResponseEntity.ok(reservations);
    }

    // Obtenir les réservations d'un client
    @GetMapping("/client/{customerId}")
    public ResponseEntity<List<ReservationDto>> obtenirReservationsParClient(
            @PathVariable Long customerId) {
        log.info("Récupération des réservations du client : {}", customerId);
        List<ReservationDto> reservations = reservationService.obtenirReservationsParClient(customerId);
        return ResponseEntity.ok(reservations);
    }

    // Obtenir les réservations d'un restaurant pour une date
    @GetMapping("/restaurant/{restaurantId}/date/{date}")
    public ResponseEntity<List<ReservationDto>> obtenirReservationsParRestaurantEtDate(
            @PathVariable Long restaurantId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Récupération des réservations du restaurant {} pour la date {}", restaurantId, date);
        List<ReservationDto> reservations = reservationService.obtenirReservationsParRestaurantEtDate(restaurantId, date);
        return ResponseEntity.ok(reservations);
    }

    // Confirmer une réservation
    @PutMapping("/{id}/confirmer")
    public ResponseEntity<ReservationDto> confirmerReservation(@PathVariable Long id) {
        log.info("Confirmation de la réservation : {}", id);
        ReservationDto reservation = reservationService.confirmerReservation(id);
        return ResponseEntity.ok(reservation);
    }

    // Annuler une réservation
    @PutMapping("/{id}/annuler")
    public ResponseEntity<ReservationDto> annulerReservation(@PathVariable Long id) {
        log.info("Annulation de la réservation : {}", id);
        ReservationDto reservation = reservationService.annulerReservation(id);
        return ResponseEntity.ok(reservation);
    }

    // Terminer une réservation
    @PutMapping("/{id}/terminer")
    public ResponseEntity<ReservationDto> terminerReservation(@PathVariable Long id) {
        log.info("Réservation terminée : {}", id);
        ReservationDto reservation = reservationService.terminerReservation(id);
        return ResponseEntity.ok(reservation);
    }

    // Marquer comme non présenté
    @PutMapping("/{id}/non-presentee")
    public ResponseEntity<ReservationDto> marquerNonPresentee(@PathVariable Long id) {
        log.info("Réservation marquée comme non présentée : {}", id);
        ReservationDto reservation = reservationService.marquerNonPresentee(id);
        return ResponseEntity.ok(reservation);
    }

    // Mettre à jour une réservation
    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto> mettreAJourReservation(
            @PathVariable Long id,
            @Valid @RequestBody CreateReservationRequest request) {
        log.info("Mise à jour de la réservation : {}", id);
        ReservationDto reservation = reservationService.mettreAJourReservation(id, request);
        return ResponseEntity.ok(reservation);
    }

    // Supprimer une réservation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerReservation(@PathVariable Long id) {
        log.info("Suppression de la réservation : {}", id);
        reservationService.annulerReservation(id);
        return ResponseEntity.noContent().build();
    }

    // Statistiques d'un restaurant
    @GetMapping("/restaurant/{restaurantId}/statistiques")
    public ResponseEntity<StatistiquesReservationDto> obtenirStatistiquesRestaurant(
            @PathVariable Long restaurantId) {
        log.info("Récupération des statistiques du restaurant : {}", restaurantId);
        StatistiquesReservationDto stats = reservationService.getStatisticsRestaurant(restaurantId);
        return ResponseEntity.ok(stats);
    }
}
