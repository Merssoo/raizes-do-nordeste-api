package com.raizesdonordeste.api.dto;

import com.raizesdonordeste.application.shared.Identificavel;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueDTO implements Identificavel<Long> {
    private Long id;
    private Integer quantidade;

    @NotNull(message = "Produto é obrigatório")
    private Long produtoId;

    @NotNull(message = "Unidade é obrigatória")
    private Long unidadeId;
}
