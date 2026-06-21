package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.api.dto.EstoqueDTO;
import com.raizesdonordeste.application.service.EstoqueService;
import com.raizesdonordeste.application.service.ProdutoService;
import com.raizesdonordeste.infrastructure.security.JwtFilter;
import com.raizesdonordeste.infrastructure.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EstoqueController.class)
class EstoqueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstoqueService estoqueService;

    @MockBean
    private ProdutoService produtoService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtFilter jwtFilter;

    @Test
    @WithMockUser(roles = "ADMIN")
    void criar_DeveRetornar201() throws Exception {
        EstoqueDTO dto = new EstoqueDTO(1L, 10, 1L, 1L);
        when(estoqueService.criar(any(EstoqueDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/estoques")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantidade\":10, \"produtoId\":1, \"unidadeId\":1}"))
                .andExpect(status().isOk());
    }
}
