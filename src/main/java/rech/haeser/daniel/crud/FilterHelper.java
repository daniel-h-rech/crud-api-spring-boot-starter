package rech.haeser.daniel.crud;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static java.util.function.Predicate.not;


@AllArgsConstructor
public class FilterHelper {

    private static final int DEFAULT_PAGE_SIZE = 100;

    private final Map<String, String> queryParams;
    private final Optional<Integer> pageIndex;
    private final Optional<Integer> pageSize;

    Optional<Pageable> getPageable() {
        if (!pageIndex.isPresent() && !pageSize.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(
                PageRequest.of(
                        pageIndex.orElse(0),
                        pageSize.orElse(DEFAULT_PAGE_SIZE),
                        getSort()
                )
        );
    }

    Sort getSort() {
        final var orderBy = queryParams.getOrDefault(ReservedQueryParam.ORDER_BY.getParamName(), "");
        final var orderByList = Arrays.stream(orderBy.split(","))
                .filter(not(String::isBlank))
                .map(Sort.Order::asc)
                .toList();
        return Sort.by(orderByList);
    }
}
