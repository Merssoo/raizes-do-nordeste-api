package com.raizesdonordeste.infrastructure.repository;
import com.raizesdonordeste.domain.entity.Pagamento;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends BaseRepository<Pagamento, Long> {
}
