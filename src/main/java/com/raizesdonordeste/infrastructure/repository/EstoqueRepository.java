package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.domain.entity.Estoque;
import org.springframework.stereotype.Repository;

@Repository
public interface EstoqueRepository extends BaseRepository<Estoque, Long> {
}
