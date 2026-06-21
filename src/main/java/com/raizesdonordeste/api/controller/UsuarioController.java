package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.api.dto.AuthenticatedUsuarioDTO;
import com.raizesdonordeste.api.dto.UsuarioDTO;
import com.raizesdonordeste.api.dto.request.CreateStaffRequest;
import com.raizesdonordeste.api.dto.request.RegisterRequest;
import com.raizesdonordeste.application.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Listar usuários", description = "Lista usuários cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de usuários")
    @GetMapping
    public ResponseEntity<Page<UsuarioDTO>> listar(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listarTodos(pageable));
    }

    @Operation(summary = "Criar cliente", description = "Registra um novo cliente")
    @ApiResponse(responseCode = "201", description = "Cliente registrado")
    @PostMapping("/create-cliente")
    public ResponseEntity<Void> criarCliente(@RequestBody @Valid RegisterRequest request) {
        usuarioService.register(request);
        return ResponseEntity.status(201).build();
    }

    @Operation(summary = "Criar staff", description = "Registra um novo membro da equipe")
    @ApiResponse(responseCode = "201", description = "Staff registrado")
    @PostMapping("/create-staff")
    public ResponseEntity<Void> criarStaff(
            @RequestBody @Valid CreateStaffRequest request,
            @AuthenticationPrincipal AuthenticatedUsuarioDTO criadorDto
    ) {
        usuarioService.criarUsuarioStaff(request, criadorDto);
        return ResponseEntity.status(201).build();
    }
}
