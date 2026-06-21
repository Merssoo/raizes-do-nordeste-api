package com.raizesdonordeste.application.service;

import com.raizesdonordeste.api.dto.EstoqueDTO;
import com.raizesdonordeste.domain.entity.Estoque;
import com.raizesdonordeste.domain.entity.Produto;
import com.raizesdonordeste.domain.entity.Unidade;
import com.raizesdonordeste.infrastructure.repository.EstoqueRepository;
import com.raizesdonordeste.infrastructure.repository.ProdutoRepository;
import com.raizesdonordeste.infrastructure.repository.UnidadeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstoqueServiceTest {

    @Mock
    private EstoqueRepository estoqueRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private UnidadeRepository unidadeRepository;

    @Mock
    private jakarta.persistence.EntityManager entityManager;

    @InjectMocks
    private EstoqueService estoqueService;

    @Test
    void criar_DeveRetornarEstoqueCriado() {
        EstoqueDTO dto = new EstoqueDTO(1L, 10, 1L, 1L);
        when(estoqueRepository.save(any(Estoque.class))).thenReturn(new Estoque());

        when(produtoRepository.findById(any())).thenReturn(Optional.of(new Produto()));
        when(unidadeRepository.findById(any())).thenReturn(Optional.of(new Unidade()));

        EstoqueDTO resultado = estoqueService.criar(dto);

        assertNotNull(resultado);
    }
}
