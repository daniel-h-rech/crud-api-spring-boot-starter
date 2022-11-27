package rech.haeser.daniel.crud;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rech.haeser.daniel.crud.exception.DataIntegrityException;

import java.util.Map;
import java.util.Set;


public abstract class CrudController<D, E extends BaseEntity<B, I>, B, I> {

    @PostMapping
    public final I create(@RequestBody final D dto) {
        return getCrudService().create(dto, rolesFor(Permission.CREATE));
    }

    @GetMapping("/{id}")
    public final ResponseEntity<D> read(@PathVariable final I id) {
        return getCrudService().read(id, rolesFor(Permission.READ))
                .map(ResponseEntity.ok()::body)
                .orElseGet(ResponseEntity.noContent()::build);
    }

    @PutMapping("/{id}")
    public final ResponseEntity<String> update(
            @PathVariable final I id,
            @RequestBody final D dto
    ) {
        try {
            return getCrudService().update(id, dto, rolesFor(Permission.UPDATE))
                    .map(i -> ResponseEntity.ok().<String>build())
                    .orElseGet(ResponseEntity.badRequest()::build);
        } catch (DataIntegrityException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public final ResponseEntity<I> delete(@PathVariable final I id) {
        return getCrudService().delete(id, rolesFor(Permission.DELETE))
                .map(ResponseEntity.ok()::body)
                .orElseGet(ResponseEntity.noContent()::build);
    }

    @GetMapping
    public final ResponseEntity<CollectionResponse<D>> getList(@RequestParam final Map<String, String> queryParams) {
        final var collectionResponse = CollectionResponse.<D>builder()
                .results(getCrudService().getList(queryParams, getSupportedFilterParams(), rolesFor(Permission.QUERY)))
                .build();
        return ResponseEntity.ok()
                .body(collectionResponse);
    }

    protected abstract CrudService<D, E, B, I> getCrudService();

    protected Set<String> getSupportedFilterParams() {
        return Set.of();
    }

    protected Set<String> rolesFor(final Permission permission) {
        return Set.of();
    }
}
