package rech.haeser.daniel.crud;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;


@AllArgsConstructor
public class FilterHelper {

    private final Set<String> supportedQueryParams;
    private final Map<String, String> queryParams;

    Map<String, String> getFilterMap() {
        return queryParams.entrySet()
                .stream()
                .filter(not(entry -> ReservedQueryParam.getAllParamNames().contains(entry.getKey())))
                .filter(entry -> {
                    if (!supportedQueryParams.contains(entry.getKey())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                    }
                    return true;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    Optional<Pageable> getPageable() {
        if (queryParams.containsKey(ReservedQueryParam.PAGE_INDEX.getParamName())
                || queryParams.containsKey(ReservedQueryParam.PAGE_SIZE.getParamName())
        ) {
            final var pageIndex = Integer.parseInt(
                    queryParams.getOrDefault(ReservedQueryParam.PAGE_INDEX.getParamName(), "0")
            );
            final var pageSize = Integer.parseInt(
                    queryParams.getOrDefault(ReservedQueryParam.PAGE_SIZE.getParamName(), "0")
            );
            return Optional.of(PageRequest.of(pageIndex, pageSize, getSort()));
        }
        return Optional.empty();
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
