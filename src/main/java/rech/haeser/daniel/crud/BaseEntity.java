package rech.haeser.daniel.crud;

public interface BaseEntity<B, I> {
    I getId();
    B toBuilder();
}
