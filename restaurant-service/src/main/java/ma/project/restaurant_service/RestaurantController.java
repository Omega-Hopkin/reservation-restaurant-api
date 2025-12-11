package ma.project.restaurant_service;

import feign.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.project.restaurant_service.dto.AvisDto;
import ma.project.restaurant_service.dto.CreateAvisRequest;
import ma.project.restaurant_service.dto.RestaurantDto;
import ma.project.restaurant_service.service.RestaurantService;
import ma.project.restaurant_service.service.GooglePlacesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.List;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
@Slf4j
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@CrossOrigin(origins = "http://localhost:4200")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final GooglePlacesService googlePlacesService;

    //créer restaurant
    @PostMapping
    public ResponseEntity<RestaurantDto> createRestaurant(@RequestBody RestaurantDto restaurantDto) {
        log.info("Création d'un nouveau restaurant : {}", restaurantDto.getNom());
        RestaurantDto createdRestaurant = restaurantService.createRestaurant(restaurantDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRestaurant);
    }

    //obtenir tous les restaurants actifs
    @GetMapping
    public ResponseEntity<List<RestaurantDto>> getAllRestaurants() {
        log.info("Récupération de tous les restaurants actifs");
        List<RestaurantDto> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long id) {
        log.info("Récupération du restaurant avec l'ID : {}", id);
        RestaurantDto restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }

    //rechercher restaurants
    @GetMapping("/search")
    public ResponseEntity<List<RestaurantDto>> searchRestaurants(@RequestParam String q) {
        log.info("Recherche de restaurants avec le terme : {}", q);
        List<RestaurantDto> restaurants = restaurantService.searchRestaurants(q);
        return ResponseEntity.ok(restaurants);
    }

    //filtrer les restau avec critères multiples
    @GetMapping("/filter")
    public ResponseEntity<List<RestaurantDto>> filterRestaurants(
        @RequestParam(required = false) String typeCuisine,
        @RequestParam(required = false) String ville,
        @RequestParam(required = false) Integer noteMin,
        @RequestParam(required = false) Integer prixMax,
        @RequestParam(required = false) Double latitude,
        @RequestParam(required = false) Double longitude,
        @RequestParam(required = false) Double distanceMax,
        @RequestParam(required = false) String triParam
    ) {
        log.info("Filtrage des restaurants avec les critètes");
        List<RestaurantDto> restaurants = restaurantService.filterRestaurants(
                typeCuisine, ville, noteMin, prixMax, latitude, longitude, distanceMax, triParam
        );
        return ResponseEntity.ok(restaurants);
    }

    //restaurants par ville
    @GetMapping("/ville/{ville}")
    public ResponseEntity<List<RestaurantDto>> getRestaurantsByVille(@PathVariable String ville) {
        log.info("Récupération des restaurants de la ville : {}", ville);
        List<RestaurantDto> restaurants = restaurantService.getRestaurantsByVille(ville);
        return ResponseEntity.ok(restaurants);
    }

    //restaurants par type de cuisine
    @GetMapping("/cuisine/{typeCuisine}")
    public ResponseEntity<List<RestaurantDto>> getRestaurantsByCuisine(@PathVariable String typeCuisine) {
        log.info("Récupération des restaurants de type cuisine : {}", typeCuisine);
        List<RestaurantDto> restaurants = restaurantService.obtenirRestaurantsParTypeCuisine(typeCuisine);
        return ResponseEntity.ok(restaurants);
    }

    //ajouter avis
    @PostMapping("/{id}/avis")
    public ResponseEntity<AvisDto> addAvisToRestaurant(@PathVariable Long id, @Valid @RequestBody CreateAvisRequest request) {
        log.info("Ajout d'un avis au restaurant {}", id);
        AvisDto avis = restaurantService.ajouterAvis(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(avis);
    }

    @GetMapping("/google/proximite")
    public Mono<ResponseEntity<List<RestaurantDto>>> searchNearbyRestaurants(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(required = false, defaultValue = "1500") Integer radius
    ) {
        log.info("Recherche des restaurants à proximité de la position ({}, {}, {})", latitude, longitude, radius);
        return googlePlacesService.rechercherRestaurantsProximite(latitude, longitude, radius)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());

                //.map(ResponseEntity::ok)

    }

    //obtenir détails d'un restau depuis Google Places API
    @GetMapping("/google/details/{placeId}")
    public Mono<ResponseEntity<RestaurantDto>> getGooglePlacesDetails(@PathVariable String placeId) {
        log.info("récupération des détails du restaurant depuis Google Places pour placeId : {}", placeId);
        return googlePlacesService.obtenirDetailsRestaurant(placeId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    //mettre à jour un restaurant
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDto> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantDto restaurantDto
    ) {
        log.info("Mise à jour du restaurant : {}", id);
        restaurantDto.setId(id);
        RestaurantDto updated = restaurantService.createRestaurant(restaurantDto);

        return ResponseEntity.ok(updated);
    }

    //supprimer un restaurant -soft delete-
    @DeleteMapping("/{id}")
    public ResponseEntity<RestaurantDto> deleteRestaurant(@PathVariable Long id) {
        log.info("Supression du restaurant : {}", id);
        RestaurantDto restaurant = restaurantService.getRestaurantById(id);
        restaurant.setActif(false);
        restaurantService.createRestaurant(restaurant);

        return ResponseEntity.noContent().build();
    }

    //supprimer un restaurant -hard delete-
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> fullDelete(@PathVariable Long id) {
//        log.info("Supression du restaurant : {}", id);
//        restaurantService.deleteRestaurant(id);
//        return ResponseEntity.noContent().build();
//    }
}