package com.raizesdonordeste.application.service;

import com.raizesdonordeste.api.dto.ProdutoDTO;
import com.raizesdonordeste.domain.entity.Produto;
import com.raizesdonordeste.infrastructure.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    void criar_DeveRetornarProdutoCriado() {
        ProdutoDTO dto = new ProdutoDTO(1L, "Tapioca", "Desc", BigDecimal.TEN, true);
        Produto produto = new Produto(1L, "Tapioca", "Desc", BigDecimal.TEN, true);
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        ProdutoDTO resultado = produtoService.criar(dto);

        assertNotNull(resultado);
    }
}
