package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.api.dto.UnidadeDTO;
import com.raizesdonordeste.application.service.UnidadeService;
import com.raizesdonordeste.domain.enums.EstadoEnum;
import com.raizesdonordeste.infrastructure.security.JwtFilter;
import com.raizesdonordeste.infrastructure.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UnidadeController.class)
class UnidadeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UnidadeService unidadeService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtFilter jwtFilter;

    @Test
    @WithMockUser(roles = "ADMIN")
    void criar_DeveRetornar201() throws Exception {
        UnidadeDTO dto = new UnidadeDTO(1L, "Unidade", "Cidade", EstadoEnum.SC, true);
        when(unidadeService.criar(any(UnidadeDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/unidades")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Unidade\", \"cidade\":\"Cidade\", \"estado\":\"SC\"}"))
                .andExpect(status().isOk());
    }
}
