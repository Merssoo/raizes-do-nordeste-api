package com.raizesdonordeste.api.dto;

import com.raizesdonordeste.application.shared.Identificavel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoDTO implements Identificavel<Long> {

    private Long id;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;
    private PedidoDTO pedidoDTO;
    private ProdutoDTO produtoDTO;
}
