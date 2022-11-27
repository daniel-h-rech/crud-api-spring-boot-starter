package rech.haeser.daniel.crud;

import lombok.Builder;

import java.util.List;

@Builder
public record CollectionResponse<T>(List<T> results) {
}
