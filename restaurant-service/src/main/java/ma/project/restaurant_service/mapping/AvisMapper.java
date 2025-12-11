package ma.project.restaurant_service.mapping;

import ma.project.restaurant_service.dto.AvisDto;
import ma.project.restaurant_service.model.Avis;
import org.springframework.stereotype.Component;

@Component
public class AvisMapper implements GenericMapper<Avis, AvisDto> {
    @Override
    public AvisDto toDto(Avis avis) {
        return AvisDto.builder()
                .id(avis.getId())
                .restaurantId(avis.getRestaurant().getId())
                .nomUtilisateur(avis.getNomUtilisateur())
                .note(avis.getNote())
                .commentaire(avis.getCommentaire())
                .dateCreation(avis.getDateCreation())
                .dateModification(avis.getDateModification())
                .build();
    }

    @Override
    public Avis toEntity(AvisDto avisDto) {
        return Avis.builder()
                .id(avisDto.getId())
                .nomUtilisateur(avisDto.getNomUtilisateur())
                .note(avisDto.getNote())
                .commentaire(avisDto.getCommentaire())
                .dateCreation(avisDto.getDateCreation())
                .dateModification(avisDto.getDateModification())
                .build();
    }
}
