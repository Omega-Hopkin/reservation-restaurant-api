package ma.project.restaurant_service.repository;

//import feign.Param;
import ma.project.restaurant_service.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false) // bach qu'on expose pas directement via Spring Data rest...
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByActifTrue();

    List<Restaurant> findByVilleAndActifTrue(String ville);

    List<Restaurant> findByTypeCuisineAndActifTrue(String typeCuisine);

    Optional<Restaurant> findByGooglePlaceId(String googlePlaceId);

    @Query("SELECT r FROM Restaurant r WHERE r.actif = true AND " +
            "LOWER(r.nom) LIKE LOWER(CONCAT('%', :recherche, '%')) OR " +
            "LOWER(r.typeCuisine) LIKE LOWER(CONCAT('%', :recherche, '%')) OR " +
            "LOWER(r.ville) LIKE LOWER(CONCAT('%', :recherche, '%'))")
    List<Restaurant> rechercherRestaurants(@Param("recherche") String recherche);


    @Query("SELECT r FROM Restaurant r WHERE r.actif = true AND " +
            "r.prixMoyen <= :prixMax ORDER BY r.noteGlobale DESC")
    List<Restaurant> findByPrixMoyenLessThanEqualAndActifTrueOrderByNoteGlobaleDesc(Integer prixMax);
}
