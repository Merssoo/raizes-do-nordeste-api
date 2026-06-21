package com.raizesdonordeste.api.dto;

import com.raizesdonordeste.application.shared.Identificavel;
import com.raizesdonordeste.domain.enums.CanalPedido;
import com.raizesdonordeste.domain.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO implements Identificavel<Long> {
    private Long id;
    private BigDecimal valorTotal;
    private CanalPedido canalPedido;
    private StatusPedido status;
    private Long clienteId;
    private Long unidadeId;
    private LocalDateTime createdAt;
}
