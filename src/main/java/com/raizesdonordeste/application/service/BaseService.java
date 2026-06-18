package com.raizesdonordeste.application.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.raizesdonordeste.infrastructure.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BaseService<T, D, ID> implements GenericService<T, D, ID> {

    protected final BaseRepository<T, ID> repository;
    protected final Class<T> entityClass;

    protected BaseService(BaseRepository<T, ID> repository, Class<T> entityClass) {
        this.repository = repository;
        this.entityClass = entityClass;
    }

    @Override
    @Transactional
    public D save(D dto) {
        T entity = toEntity(dto);
        T savedEntity = repository.save(entity);
        return toDto(savedEntity);
    }

    @Override
    @Transactional
    public D update(ID id, D dto) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Registro não encontrado com o ID: " + id);
        }
        T entity = toEntity(dto);
        T savedEntity = repository.save(entity);
        return toDto(savedEntity);
    }

    @Override
    @Transactional
    public void delete(ID id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<D> getById(ID id) {
        return repository.findById(id).map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<D> getAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<D> getPaged(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<D> getPaged(Predicate predicate, Pageable pageable) {
        return repository.findAll(predicate, pageable).map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<D> getPaged(String filter, Pageable pageable) {
        return getPaged(filter, null, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<D> getPaged(String filter, Predicate predicate, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (predicate != null) {
            builder.and(predicate);
        }

        if (filter != null && filter.contains("=")) {
            PathBuilder<T> pathBuilder = new PathBuilder<>(entityClass, entityClass.getSimpleName().toLowerCase());
            String[] parts = filter.split("=");
            if (parts.length >= 2) {
                String campo = parts[0];
                String valor = parts[1];

                try {
                    StringPath path = pathBuilder.getString(campo);
                    builder.and(path.containsIgnoreCase(valor));
                } catch (Exception e) {
                    throw new RuntimeException("Campo de filtro inválido: " + campo);
                }
            }
        }

        return repository.findAll(builder, pageable).map(this::toDto);
    }

    public abstract T toEntity(D dto);
    public abstract D toDto(T entity);
}
