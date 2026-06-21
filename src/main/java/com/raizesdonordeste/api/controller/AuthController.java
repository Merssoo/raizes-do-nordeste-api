package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.api.dto.request.LoginRequest;
import com.raizesdonordeste.api.dto.request.RegisterRequest;
import com.raizesdonordeste.api.dto.response.AuthResponse;
import com.raizesdonordeste.application.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para registro e autenticação de usuários")
public class AuthController {

    private final UsuarioService authService;

    @Operation(summary = "Registrar novo cliente", description = "Cadastra um usuário com perfil de cliente")
    @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(201).build();
    }

    @Operation(summary = "Registrar administrador", description = "Cadastra um administrador (requer secret key)")
    @ApiResponse(responseCode = "201", description = "Administrador cadastrado com sucesso")
    @PostMapping("/register-admin")
    public ResponseEntity<Void> registerAdmin(
            @Parameter(in = ParameterIn.HEADER, name = "X-Admin-Secret-Key", required = true)
            @RequestBody @Valid RegisterRequest request,
            @RequestHeader("X-Admin-Secret-Key") String secretKey
    ) {
        authService.registerAdmin(request, secretKey);
        return ResponseEntity.status(201).build();
    }

    @Operation(summary = "Login", description = "Autentica usuário e retorna token JWT")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
