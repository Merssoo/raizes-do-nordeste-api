package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.api.dto.UsuarioDTO;
import com.raizesdonordeste.api.dto.request.CreateClienteRequest;
import com.raizesdonordeste.api.dto.request.CreateStaffRequest;
import com.raizesdonordeste.application.service.UsuarioService;
import com.raizesdonordeste.domain.entity.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Page<UsuarioDTO>> listar(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listarTodos(pageable));
    }

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
