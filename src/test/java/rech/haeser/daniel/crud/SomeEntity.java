package rech.haeser.daniel.crud;

import lombok.Builder;

@Builder(toBuilder = true)
public class SomeEntity implements BaseEntity<SomeEntity.SomeEntityBuilder, Long>{
    @Override
    public Long getId() {
        return 1L;
    }
}
