package rech.haeser.daniel.crud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JpaCriteriaRepository<E, I> extends JpaRepository<E, I>, JpaSpecificationExecutor<E> {
}
