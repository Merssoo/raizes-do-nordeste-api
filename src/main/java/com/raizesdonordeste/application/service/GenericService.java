package com.raizesdonordeste.application.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface GenericService<T, D, ID> {
    D save(D dto);
    D update(ID id, D dto);
    void delete(ID id);
    Optional<D> getById(ID id);
    List<D> getAll();
    Page<D> getPaged(Pageable pageable);
    Page<D> getPaged(Predicate predicate, Pageable pageable);
    Page<D> getPaged(String filter, Pageable pageable);
    Page<D> getPaged(String filter, Predicate predicate, Pageable pageable);

    T toEntity(D dto);
    D toDto(T entity);
}
