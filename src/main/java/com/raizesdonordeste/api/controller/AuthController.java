package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.api.dto.request.LoginRequest;
import com.raizesdonordeste.api.dto.request.RegisterRequest;
import com.raizesdonordeste.api.dto.response.AuthResponse;
import com.raizesdonordeste.application.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody @Valid RegisterRequest request
    ) {
        authService.register(request);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid LoginRequest request
    ) {
        return ResponseEntity.ok(
                authService.login(request)
        );
    }
}
