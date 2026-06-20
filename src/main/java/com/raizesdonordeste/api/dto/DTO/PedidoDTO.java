package com.raizesdonordeste.api.dto.DTO;

import com.raizesdonordeste.domain.enums.CanalPedido;
import com.raizesdonordeste.domain.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Long id;
    private BigDecimal valorTotal;
    private CanalPedido canalPedido;
    private StatusPedido status;
    private Long clienteId;
    private Long unidadeId;
}
