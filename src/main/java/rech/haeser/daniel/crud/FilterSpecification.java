package rech.haeser.daniel.crud;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class FilterSpecification<E> implements Specification<E> {

    private final Map<String, String> filterParams;

    @Override
    public Predicate toPredicate(
            final Root root,
            final CriteriaQuery query,
            final CriteriaBuilder criteriaBuilder
    ) {
        final var predicates = filterParams.entrySet()
                .stream()
                .map(entry -> criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()))
                .collect(Collectors.toSet());
        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }
}
