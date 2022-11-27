package rech.haeser.daniel.crud;

public interface Mapper<D, E> {
    D toDto(final E entity);

    E toEntity(final D dto);

    E updateEntity(final E entity, final D dto);
}
