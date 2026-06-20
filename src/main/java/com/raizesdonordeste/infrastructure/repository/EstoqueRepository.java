package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.domain.entity.Estoque;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstoqueRepository extends BaseRepository<Estoque, Long> {
    List<Estoque> findByUnidadeId(Long unidadeId);
}
