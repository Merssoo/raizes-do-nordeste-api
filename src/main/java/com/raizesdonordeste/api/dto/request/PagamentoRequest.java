package com.raizesdonordeste.api.dto.request;
import jakarta.validation.constraints.NotNull;

public record PagamentoRequest(
        @NotNull(message = "Pedido ID é obrigatório")
        Long pedidoId,
        @NotNull(message = "Forma de pagamento é obrigatória")
        String formaPagamento
) {}
