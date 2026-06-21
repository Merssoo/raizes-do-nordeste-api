package com.raizesdonordeste.api.dto;

import com.raizesdonordeste.application.shared.Identificavel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO implements Identificavel<Long> {
    private Long id;

    @NotNull(message = "Nome é obrigatório")
    private String nome;

    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    private BigDecimal preco;

    private Boolean ativo;
}
