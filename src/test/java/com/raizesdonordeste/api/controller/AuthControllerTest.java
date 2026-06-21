package com.raizesdonordeste.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raizesdonordeste.api.dto.request.LoginRequest;
import com.raizesdonordeste.api.dto.request.RegisterRequest;
import com.raizesdonordeste.api.dto.response.AuthResponse;
import com.raizesdonordeste.application.service.UsuarioService;
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

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtFilter jwtFilter;


    @Test
    @WithMockUser
    void registerShouldReturn201() throws Exception {
        RegisterRequest request = new RegisterRequest("Nome", "email@test.com", "123456");
        
        mockMvc.perform(post("/auth/register").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void loginShouldReturn200() throws Exception {
        LoginRequest request = new LoginRequest("email@test.com", "123456");
        when(usuarioService.login(any(LoginRequest.class))).thenReturn(new AuthResponse("token"));

        mockMvc.perform(post("/auth/login").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
