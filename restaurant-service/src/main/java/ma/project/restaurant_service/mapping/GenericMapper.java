package ma.project.restaurant_service.mapping;

public interface GenericMapper<E, D> {
    E toEntity(D dto);
    D toDto(E entity);
}
