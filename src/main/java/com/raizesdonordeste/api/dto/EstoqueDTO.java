package com.raizesdonordeste.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueDTO {
    private Long id;
    private Integer quantidade;
    private Long produtoId;
    private Long unidadeId;
}
