package com.raizesdonordeste.api.dto;

import com.raizesdonordeste.application.shared.Identificavel;
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
public class PagamentoDTO implements Identificavel<Long> {

    private Long id;
    private BigDecimal valor;
    private String status;
    private String formaPagamento;
    private LocalDateTime dataPagamento;
    private PedidoDTO pedidoDTO;
}
