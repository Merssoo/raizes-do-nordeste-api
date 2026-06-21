package com.raizesdonordeste.application.service;

import com.raizesdonordeste.api.dto.UnidadeDTO;
import com.raizesdonordeste.domain.entity.Unidade;
import com.raizesdonordeste.domain.enums.EstadoEnum;
import com.raizesdonordeste.infrastructure.repository.UnidadeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnidadeServiceTest {

    @Mock
    private UnidadeRepository unidadeRepository;

    @InjectMocks
    private UnidadeService unidadeService;

    private UnidadeDTO unidadeDTO;
    private Unidade unidade;

    @BeforeEach
    void setUp() {
        unidadeDTO = new UnidadeDTO(1L, "Unidade Teste", "Cidade Teste", EstadoEnum.SC, true);
        unidade = new Unidade(1L, "Unidade Teste", "Cidade Teste", EstadoEnum.SC, true);
    }

    @Test
    void criar_DeveRetornarUnidadeCriada() {
        when(unidadeRepository.save(any(Unidade.class))).thenReturn(unidade);

        UnidadeDTO resultado = unidadeService.criar(unidadeDTO);

        assertNotNull(resultado);
        assertEquals(unidadeDTO.getNome(), resultado.getNome());
        verify(unidadeRepository, times(1)).save(any(Unidade.class));
    }
}
