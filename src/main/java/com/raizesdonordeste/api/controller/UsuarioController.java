package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.api.dto.request.CreateClienteRequest;
import com.raizesdonordeste.api.dto.request.CreateStaffRequest;
import com.raizesdonordeste.application.service.UsuarioService;
import com.raizesdonordeste.domain.entity.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/create-cliente")
    public ResponseEntity<Void> criarCliente(@RequestBody @Valid CreateClienteRequest request) {
        usuarioService.criarCliente(request);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/create-staff")
    public ResponseEntity<Void> criarStaff(
            @RequestBody @Valid CreateStaffRequest request,
            @AuthenticationPrincipal Usuario criador
    ) {
        usuarioService.criarUsuarioStaff(request, criador);
        return ResponseEntity.status(201).build();
    }
}
