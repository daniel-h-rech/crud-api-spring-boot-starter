package rech.haeser.daniel.crud;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import rech.haeser.daniel.crud.exception.DataIntegrityException;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CrudServiceTest {

    @InjectMocks
    private SomeCrudService crudService;

    @Mock
    private JpaCriteriaRepository jpaCriteriaRepository;
    @Mock
    private Mapper mapper;

    @Test
    void create() {
        final var someDto = new SomeDto();
        final var someEntity = SomeEntity.builder()
                .build();
        given(mapper.toEntity(someDto))
                .willReturn(someEntity);
        given(jpaCriteriaRepository.save(someEntity))
                .willReturn(someEntity);

        final var id = crudService.create(someDto, Set.of());

        verify(jpaCriteriaRepository).save(someEntity);
        assertThat(id).isEqualTo(someEntity.getId());
    }

    @Test
    void read() {
        final var someDto = new SomeDto();
        final var someEntity = SomeEntity.builder()
                .build();
        given(jpaCriteriaRepository.findById(someEntity.getId()))
                .willReturn(Optional.of(someEntity));
        given(mapper.toDto(someEntity))
                .willReturn(someDto);

        final var actualDto = crudService.read(someEntity.getId(), Set.of());

        verify(jpaCriteriaRepository).findById(someEntity.getId());
        assertThat(actualDto).hasValue(someDto);
    }

    @Test
    void read_whenNoEntityIsFound() {
        final var someEntity = SomeEntity.builder()
                .build();
        given(jpaCriteriaRepository.findById(someEntity.getId()))
                .willReturn(Optional.empty());

        final var actualDto = crudService.read(someEntity.getId(), Set.of());

        verify(jpaCriteriaRepository).findById(someEntity.getId());
        assertThat(actualDto).isEmpty();
    }

    @Test
    void update() {
        final var someDto = new SomeDto();
        final var someEntity = SomeEntity.builder()
                .build();
        given(jpaCriteriaRepository.findById(someEntity.getId()))
                .willReturn(Optional.of(someEntity));
        given(jpaCriteriaRepository.save(someEntity))
                .willReturn(someEntity);
        given(mapper.updateEntity(someEntity, someDto))
                .willReturn(someEntity);

        final var actualId = crudService.update(someEntity.getId(), someDto, Set.of());

        verify(jpaCriteriaRepository).findById(someEntity.getId());
        verify(jpaCriteriaRepository).save(someEntity);
        assertThat(actualId).hasValue(someEntity.getId());
    }

    @Test
    void update_whenDataIntegrityExceptionIsThrown() {
        final var someDto = new SomeDto();
        final var someEntity = SomeEntity.builder()
                .build();
        final var id = someEntity.getId();
        given(jpaCriteriaRepository.findById(id))
                .willReturn(Optional.of(someEntity));
        given(jpaCriteriaRepository.save(someEntity))
                .willThrow(DataIntegrityViolationException.class);
        given(mapper.updateEntity(someEntity, someDto))
                .willReturn(someEntity);
        final var emptySet = Set.<String>of();

        assertThatThrownBy(() -> crudService.update(id, someDto, emptySet))
                .isInstanceOf(DataIntegrityException.class);
    }

    @Test
    void delete() {
        final var someEntity = SomeEntity.builder()
                .build();
        given(jpaCriteriaRepository.findById(someEntity.getId()))
                .willReturn(Optional.of(someEntity));

        final var actualId = crudService.delete(someEntity.getId(), Set.of());

        verify(jpaCriteriaRepository).findById(someEntity.getId());
        verify(jpaCriteriaRepository).delete(someEntity);
        assertThat(actualId).hasValue(someEntity.getId());
    }
}
