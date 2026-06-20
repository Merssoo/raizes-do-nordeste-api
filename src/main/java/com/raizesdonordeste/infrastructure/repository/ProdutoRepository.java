package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.domain.entity.Produto;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends BaseRepository<Produto, Long> {
}
