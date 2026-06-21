package com.raizesdonordeste.application.service;

import com.raizesdonordeste.api.dto.UsuarioDTO;
import com.raizesdonordeste.domain.entity.Usuario;
import com.raizesdonordeste.infrastructure.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void listarTodos_DeveRetornarPaginaDeUsuarios() {
        when(usuarioRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        Page<UsuarioDTO> resultado = usuarioService.listarTodos(Pageable.unpaged());

        assertNotNull(resultado);
    }
}
