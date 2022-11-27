package rech.haeser.daniel.crud;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SomeCrudService extends CrudService<SomeDto, SomeEntity, SomeEntity.SomeEntityBuilder, Long> {

    private final JpaCriteriaRepository jpaCriteriaRepository;
    private final Mapper mapper;

    @Override
    protected JpaCriteriaRepository<SomeEntity, Long> getJpaCriteriaRepository() {
        return jpaCriteriaRepository;
    }

    @Override
    protected Mapper<SomeDto, SomeEntity> getMapper() {
        return mapper;
    }
}
