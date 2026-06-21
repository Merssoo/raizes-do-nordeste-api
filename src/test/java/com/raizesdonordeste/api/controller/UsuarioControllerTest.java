package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.application.service.UsuarioService;
import com.raizesdonordeste.infrastructure.security.JwtFilter;
import com.raizesdonordeste.infrastructure.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtFilter jwtFilter;

    @Test
    @WithMockUser(roles = "ADMIN")
    void listar_DeveRetornar200() throws Exception {
        when(usuarioService.listarTodos(any())).thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/usuarios")
                .with(csrf()))
                .andExpect(status().isOk());
    }
}
