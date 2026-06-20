package com.raizesdonordeste.api.dto;

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
    private String estado;
    private Boolean ativo;
}
