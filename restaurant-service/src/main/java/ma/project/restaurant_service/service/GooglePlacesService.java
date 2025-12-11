package ma.project.restaurant_service.service;

import lombok.extern.slf4j.Slf4j;
import ma.project.restaurant_service.dto.RestaurantDto;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GooglePlacesService {
    private final WebClient webClient;

    @Value("${google.places.api.key}")
    private String apiKey;

    public GooglePlacesService(WebClient.Builder webClientBuilder,
                               @Value("${google.places.api.key}") String apiKey) {
        this.webClient = webClientBuilder.baseUrl("https://maps.googleapis.com/maps/api/place").build();

    }

    public Mono<List<RestaurantDto>> rechercherRestaurantsProximite(double latitude, double longitude, int radius) {
        String location = latitude + "," + longitude;

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/nearbysearch/json")
                    .queryParam("location", location)
                    .queryParam("radius",  radius)
                    .queryParam("type",  "restaurant")
                    .queryParam("key",  apiKey)
                    .build())
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

                    return results.stream()
                            .map(this::convertirEnRestaurantDto)
                            .toList();
                })
                .doOnError(error ->
                    log.error("Erreur lors de la récupération des restaurants à proximité : {}", error));
    }

    public Mono<RestaurantDto> obtenirDetailsRestaurant(String placeId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/details/json")
                        .queryParam("place_id", placeId)
                        .queryParam("fields", "name,formatted_address,geometry,rating," + "user_ratting_total,price_level,formatted_phone_number," + "openning_hours,photos,types")
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    Map<String, Object> result = (Map<String, Object>) response.get("results");
                    return convertirEnRestaurantDto(result);
                })
                .doOnError(error -> log.error("Erreur lors de la récupération de détails", error));
    }

    public RestaurantDto convertirEnRestaurantDto(Map<String, Object> placeData) {
        Map<String, Object> geometry = (Map<String, Object>) placeData.get("geometry");
        Map<String, Object> location = (Map<String, Object>) geometry.get("location");

        return RestaurantDto.builder()
                .nom((String) placeData.get("name"))
                .adresse((String) placeData.get("formatted_address"))
                .latitude(((Double) location.get("lat")))
                .googlePlaceId((String) placeData.get("place_d"))
                .noteGlobale(placeData.containsKey("rating") ? ((Number) placeData.get("rating")).doubleValue() : 0.0)
                .nombreAvis(placeData.containsKey("user_ratings_total") ? ((Number) placeData.get("user_ratings_total")).intValue() : 0)
                .prixMoyen(placeData.containsKey("price_level") ? ((Number) placeData.get("price_level")).intValue() : null)
                .build();
    }
}
