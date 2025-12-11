package ma.project.reservation_service.utils;

import lombok.*;
import ma.project.reservation_service.dto.CustomerFeignDto;

@Data
@Builder
public class CustomerSafeFactory {
    public static CustomerFeignDto safeCustomer(Long id, String email) {
        return CustomerFeignDto.builder()
                .id(id)
                .nom("inconnu")
                .prenom("")
                .email(email)
                .telephone("")
                .adresse("inconnue")
                .ville("inconnue")
                .pointsFidelite(0)
                .build();
    }
}
