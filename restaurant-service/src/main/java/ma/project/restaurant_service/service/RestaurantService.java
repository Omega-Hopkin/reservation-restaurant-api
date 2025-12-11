package ma.project.restaurant_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.project.restaurant_service.dto.*;
import ma.project.restaurant_service.mapping.AvisMapper;
import ma.project.restaurant_service.mapping.RestaurantMapper;
import ma.project.restaurant_service.model.Avis;
import ma.project.restaurant_service.model.Restaurant;
import ma.project.restaurant_service.repository.AvisRepository;
import ma.project.restaurant_service.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final AvisRepository avisRepository;
    private final RestaurantMapper restaurantMapper;
    private final AvisMapper avisMapper;

    //créer restaurant
    public RestaurantDto createRestaurant(RestaurantDto restaurantDto) {
        Restaurant restaurant = restaurantMapper.toEntity(restaurantDto);

        restaurant = restaurantRepository.save(restaurant);
        log.info("Restaurant créé avec succès : {}", restaurant.getId());
        return restaurantMapper.toDto(restaurant);
    }

    //récupérer tous les restau actifs
    public List<RestaurantDto> getAllRestaurants() {
        return restaurantRepository.findByActifTrue()
                .stream()
                .map(restaurantMapper::toDto) //.map(this::mapToDto)
                .toList(); //pour java 16+ | sinon dir .collect(Collectors.toList());
    }

    public RestaurantDto getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant non trouvé avec l'ID : " + id));

        return restaurantMapper.toDto(restaurant);
    }

    //rechercher restaurants
    public List<RestaurantDto> searchRestaurants(String recherche) {
        return restaurantRepository.rechercherRestaurants(recherche)
                .stream()
                .map(restaurantMapper::toDto)
                .toList();
    }

    //filtrer par ville
    public List<RestaurantDto> getRestaurantsByVille(String ville) {
        return restaurantRepository.findByVilleAndActifTrue(ville)
                .stream()
                .map(restaurantMapper::toDto)
                .toList();
    }

    //filtrer par type de cuisine
    public List<RestaurantDto> obtenirRestaurantsParTypeCuisine(String typeCuisine) {
        return restaurantRepository.findByTypeCuisineAndActifTrue(typeCuisine)
                .stream()
                .map(restaurantMapper::toDto)
                .toList();
    }

    public List<RestaurantDto> filterRestaurants(String typeCuisine, String ville, Integer noteMin, Integer prixMax, Double latitudeUser, Double longitudeUser, Double distanceMax, String triPar) {
        List<Restaurant> restaurants = restaurantRepository.findByActifTrue();

        //filtrage par type cuisine
        if (typeCuisine != null && !typeCuisine.isEmpty()) {
            restaurants = restaurants.stream()
                    .filter(r -> r.getTypeCuisine().equalsIgnoreCase(typeCuisine))
                    .toList();
        }

        if (prixMax != null) {
            restaurants = restaurants.stream()
                    .filter(r -> r.getPrixMoyen() != null && r.getPrixMoyen() <= prixMax)
                    .toList();
        }

        // Filtrage par note minimale
        if (noteMin != null) {
            restaurants = restaurants.stream()
                    .filter(r -> r.getNoteGlobale() >= noteMin)
                    .toList();
        }

        // Filtrage par ville
        if (ville != null && !ville.isEmpty()) {
            restaurants = restaurants.stream()
                    .filter(r -> r.getVille().equalsIgnoreCase(ville))
                    .collect(Collectors.toList());
        }

        List<RestaurantDto> resultats = restaurants.stream()
                .map(restaurantMapper::toDto)
                .toList();

        //calcule la distance et position user fournie
        if (latitudeUser != null && longitudeUser != null) {
            resultats.forEach(dto -> {
                double distance = calculerDistance(
                        latitudeUser, longitudeUser,
                        dto.getLatitude(), dto.getLongitude()
                );
                dto.setDistance(distance);
            });

            // Filtrage par distance maximale
            if (distanceMax != null) {
                resultats = resultats.stream()
                        .filter(dto -> dto.getDistance() <= distanceMax)
                        .toList();
            }
        }

        //tri des résultats
        if (triPar != null) {
            switch (triPar.toLowerCase()) {
                case "popularite":
                case "note":
                    resultats = new ArrayList<>(resultats); //c'est ajouté pour convertir avant le tri et éviter le warning "immutable object is modified"
                    resultats.sort(Comparator.comparing(RestaurantDto::getNoteGlobale).reversed());
                    break;
                case "distance":
                    if (latitudeUser != null && longitudeUser != null) {
                        resultats = new ArrayList<>(resultats);
                        resultats.sort(Comparator.comparing(RestaurantDto::getDistance));
                    }
                    break;
                case "prix":
                    resultats = new ArrayList<>(resultats);
                    resultats.sort(Comparator.comparing(RestaurantDto::getPrixMoyen,
                            Comparator.nullsLast(Comparator.naturalOrder())));
                    break;
                case "avis":
                    resultats = new ArrayList<>(resultats);
                    resultats.sort(Comparator.comparing(RestaurantDto::getNombreAvis).reversed());
                    break;
            }
        }

        return resultats;
    }

    //calculer la distance entre deux points (formule haversine)
    private double calculerDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; //rayon de la Terre en km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    //ajouter un avis
    public AvisDto ajouterAvis(Long restaurantId, CreateAvisRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant non trouvé"));

        Avis avis = Avis.builder()
                .restaurant(restaurant)
                .nomUtilisateur(request.getNomUtilisateur())
                .note(request.getNote())
                .commentaire(request.getCommentaire())
                .build();

        restaurant.ajouterAvis(avis);
        avis = avisRepository.save(avis);
        restaurantRepository.save(restaurant);

        log.info("Avis ajouté pour le restaurant {}", restaurantId);

        return avisMapper.toDto(avis);
    }

    //obtenir avis d'un restaurant
    public List<AvisDto> obtenirAvisParRestaurant(Long restaurantId) {
        return avisRepository.findByRestaurantIdOrderByDateCreationDesc(restaurantId).stream()
                .map(avisMapper::toDto)
                .toList();
    }

    //delete restaurant
    public void deleteRestaurant(Long restaurantId) {
        //restaurantRepository.deleteById(restaurantId); <- cette ligne seul peut suffire aussi

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant non trouvé " + restaurantId));

        restaurantRepository.delete(restaurant);
    }
}
