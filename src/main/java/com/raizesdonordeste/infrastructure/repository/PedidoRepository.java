package com.raizesdonordeste.infrastructure.repository;
import com.raizesdonordeste.domain.entity.Pedido;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends BaseRepository<Pedido, Long> {
}
