package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.api.dto.PedidoDTO;
import com.raizesdonordeste.application.service.PedidoService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtFilter jwtFilter;

    @Test
    @WithMockUser(roles = "CLIENTE")
    void listar_DeveRetornar200() throws Exception {
        mockMvc.perform(get("/pedidos")
                .with(csrf()))
                .andExpect(status().isOk());
    }
}
