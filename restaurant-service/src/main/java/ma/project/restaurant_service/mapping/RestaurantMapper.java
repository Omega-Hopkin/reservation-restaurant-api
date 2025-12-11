package ma.project.restaurant_service.mapping;

import ma.project.restaurant_service.dto.RestaurantDto;
import ma.project.restaurant_service.model.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper implements GenericMapper<Restaurant, RestaurantDto> {
    @Override
    public Restaurant toEntity(RestaurantDto dto) {
        return Restaurant.builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .adresse(dto.getAdresse())
                .ville(dto.getVille())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .telephone(dto.getTelephone())
                .email(dto.getEmail())
                .description(dto.getDescription())
                .typeCuisine(dto.getTypeCuisine())
                .prixMoyen(dto.getPrixMoyen())
                .capacite(dto.getCapacite())
                .heureOuverture(dto.getHeureOuverture())
                .heureFermeture(dto.getHeureFermeture())
                .googlePlaceId(dto.getGooglePlaceId())
                .noteGlobale(dto.getNoteGlobale() != null ? dto.getNoteGlobale() : 0.0)
                .nombreAvis(dto.getNombreAvis() != null ? dto.getNombreAvis() : 0)
                .actif(dto.getActif() != null ? dto.getActif() : true)
                .imagesUrls(dto.getImagesUrls())
                .joursFermeture(dto.getJoursFermeture())
                .build();
    }

    @Override
    public RestaurantDto toDto(Restaurant restaurant) {
        return RestaurantDto.builder()
                .id(restaurant.getId())
                .nom(restaurant.getNom())
                .adresse(restaurant.getAdresse())
                .ville(restaurant.getVille())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .telephone(restaurant.getTelephone())
                .email(restaurant.getEmail())
                .description(restaurant.getDescription())
                .typeCuisine(restaurant.getTypeCuisine())
                .prixMoyen(restaurant.getPrixMoyen())
                .capacite(restaurant.getCapacite())
                .heureOuverture(restaurant.getHeureOuverture())
                .heureFermeture(restaurant.getHeureFermeture())
                .googlePlaceId(restaurant.getGooglePlaceId())
                .noteGlobale(restaurant.getNoteGlobale())
                .nombreAvis(restaurant.getNombreAvis())
                .actif(restaurant.getActif())
                .imagesUrls(restaurant.getImagesUrls())
                .joursFermeture(restaurant.getJoursFermeture())
                .build();
    }
}