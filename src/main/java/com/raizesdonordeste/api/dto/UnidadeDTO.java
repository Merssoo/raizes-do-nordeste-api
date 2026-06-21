package com.raizesdonordeste.api.dto;

import com.raizesdonordeste.domain.enums.EstadoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeDTO {
    private Long id;
    private String nome;
    private String cidade;
    private EstadoEnum estado;
    private Boolean ativo;
}
