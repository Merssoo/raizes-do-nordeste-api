package com.raizesdonordeste.api.dto.request;
import com.raizesdonordeste.domain.enums.CanalPedido;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PedidoRequest(
        @NotNull(message = "Unidade é obrigatória")
        Long unidadeId,
        @NotNull(message = "Canal do pedido é obrigatório")
        CanalPedido canalPedido,
        @NotEmpty(message = "Itens do pedido não podem estar vazios")
        List<ItemPedidoRequest> itens,
        Long idCliente
) {}
