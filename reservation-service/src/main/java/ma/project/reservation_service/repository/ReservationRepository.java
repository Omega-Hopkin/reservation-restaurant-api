package ma.project.reservation_service.repository;

import ma.project.reservation_service.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ma.project.reservation_service.StatusReservation;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByCodeConfirmation(String codeConfirmation);

    List<Reservation> findByRestaurantId(Long restaurantId);

    List<Reservation> findByCustomerId(Long customerId);

    List<Reservation> findByRestaurantIdAndDateReservation(Long restaurantId, LocalDate date);

    List<Reservation> findByRestaurantIdAndDateReservationAndStatut(
            Long restaurantId, LocalDate date, StatusReservation statut);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.restaurantId = :restaurantId " +
            "AND r.dateReservation = :date AND r.statut IN ('EN_ATTENTE', 'CONFIRMEE')")
    Long compterReservationsActives(
            @Param("restaurantId") Long restaurantId,
            @Param("date") LocalDate date);

    @Query("SELECT SUM(r.nombrePersonnes) FROM Reservation r WHERE r.restaurantId = :restaurantId " +
            "AND r.dateReservation = :date AND r.statut IN ('EN_ATTENTE', 'CONFIRMEE')")
    Integer compterPersonnesReservees(
            @Param("restaurantId") Long restaurantId,
            @Param("date") LocalDate date);
}
