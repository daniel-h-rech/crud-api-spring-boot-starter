package rech.haeser.daniel.crud;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import rech.haeser.daniel.crud.exception.DataIntegrityException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class CrudService<D, E extends BaseEntity<B, I>, B, I> {

    @Transactional
    @PreAuthorize("hasAnyAuthority(#allowedRoles)")
    public final I create(
            final D dto,
            final Set<String> allowedRoles
    ) {
        final var entity = getMapper().toEntity(dto);
        return getJpaCriteriaRepository().save(entity)
                .getId();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority(#allowedRoles)")
    public Optional<D> read(
            final I id,
            final Set<String> allowedRoles
    ) {
        return getJpaCriteriaRepository().findById(id)
                .map(getMapper()::toDto);
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority(#allowedRoles)")
    public final Optional<I> update(
            final I id,
            final D dto,
            final Set<String> allowedRoles
    ) {
        try {
            return getJpaCriteriaRepository()
                    .findById(id)
                    .map(entity -> getMapper().updateEntity(entity, dto))
                    .map(getJpaCriteriaRepository()::save)
                    .map(BaseEntity::getId);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e);
        }
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority(#allowedRoles)")
    public final Optional<I> delete(
            final I id,
            final Set<String> allowedRoles
    ) {
        return getJpaCriteriaRepository().findById(id)
                .map(entity -> {
                    getJpaCriteriaRepository().delete(entity);
                    return entity.getId();
                });
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority(#allowedRoles)")
    public final List<D> query(
            final Map<String, String> filterParams,
            final Optional<Integer> pageIndex,
            final Optional<Integer> pageSize,
            final Set<String> allowedRoles
    ) {
        final var filterHelper = new FilterHelper(filterParams, pageIndex, pageSize);
        final var specification = new FilterSpecification<E>(filterParams);
        return filterHelper.getPageable()
                .map(pageable -> getJpaCriteriaRepository().findAll(specification, pageable).stream())
                .orElseGet(getJpaCriteriaRepository().findAll(specification, filterHelper.getSort())::stream)
                .map(getMapper()::toDto)
                .toList();
    }

    protected abstract JpaCriteriaRepository<E, I> getJpaCriteriaRepository();

    protected abstract Mapper<D, E> getMapper();
}
