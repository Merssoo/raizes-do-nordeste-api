package com.raizesdonordeste.api.dto.request;
import com.raizesdonordeste.domain.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record StatusPedidoRequest(
        @NotNull(message = "Status é obrigatório")
        StatusPedido status
) {}
