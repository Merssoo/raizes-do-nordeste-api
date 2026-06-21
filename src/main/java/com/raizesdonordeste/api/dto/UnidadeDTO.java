package com.raizesdonordeste.api.dto;

import com.raizesdonordeste.application.shared.Identificavel;
import com.raizesdonordeste.domain.enums.EstadoEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeDTO implements Identificavel<Long> {
    private Long id;

    @NotNull(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Endereço é obrigatório")
    private String cidade;

    @NotNull(message = "Estado é obrigatório")
    private EstadoEnum estado;

    private Boolean ativo;
}
