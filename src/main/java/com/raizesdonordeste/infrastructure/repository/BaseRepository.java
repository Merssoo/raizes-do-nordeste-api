package com.raizesdonordeste.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.raizesdonordeste.api.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>,
        JpaSpecificationExecutor<T>,
        QuerydslPredicateExecutor<T> {

    default Page<T> findAllWithFilter(String filter, Predicate predicate, Class<T> entityClass, Pageable pageable) {
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
                    throw new IllegalArgumentException("Campo de filtro inválido: " + campo);
                }
            }
        }

        return findAll(builder, pageable);
    }

    default Page<T> findAllWithFilterQuery(JPAQuery<T> query, String filter, Class<T> entityClass, Pageable pageable) {
        if (filter != null && filter.contains("=")) {
            PathBuilder<T> pathBuilder = new PathBuilder<>(entityClass, entityClass.getSimpleName().toLowerCase());
            String[] parts = filter.split("=");
            if (parts.length >= 2) {
                String campo = parts[0];
                String valor = parts[1];
                try {
                    query.where(pathBuilder.getString(campo).containsIgnoreCase(valor));
                } catch (Exception e) {
                    throw new IllegalArgumentException("Campo de filtro inválido: " + campo);
                }
            }
        }

        long total = query.fetchCount();
        List<T> content = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }
}
