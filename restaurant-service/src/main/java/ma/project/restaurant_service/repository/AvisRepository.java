package ma.project.restaurant_service.repository;

import ma.project.restaurant_service.model.Avis;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AvisRepository extends JpaRepository<Avis, Long> {
    List<Avis> findByRestaurantIdOrderByDateCreationDesc(Long restaurantId);

    List<Avis> findByNomUtilisateur(String nomUtilisateur);
}