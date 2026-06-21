package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.api.dto.UnidadeDTO;
import com.raizesdonordeste.application.service.UnidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/unidades")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Unidades", description = "Endpoints para gerenciamento das filiais")
public class UnidadeController {

    private final UnidadeService service;

    @Operation(summary = "Criar unidade", description = "Cadastra uma nova filial")
    @ApiResponse(responseCode = "201", description = "Unidade criada")
    @PostMapping
    public ResponseEntity<UnidadeDTO> criar(@RequestBody @Valid UnidadeDTO unidadeDto) {
        return ResponseEntity.status(201).body(service.criar(unidadeDto));
    }

    @Operation(summary = "Atualizar unidade", description = "Atualiza dados de uma filial")
    @ApiResponse(responseCode = "200", description = "Unidade atualizada")
    @PutMapping("/{id}")
    public ResponseEntity<UnidadeDTO> atualizar(@PathVariable Long id, @RequestBody @Valid UnidadeDTO unidadeDto) {
        return ResponseEntity.ok(service.atualizar(id, unidadeDto));
    }

    @Operation(summary = "Listar unidades", description = "Lista todas unidades cadastradas")
    @ApiResponse(responseCode = "200", description = "Lista de unidades")
    @GetMapping
    public ResponseEntity<Page<UnidadeDTO>> listarTodos(
            @RequestParam(required = false) String filter,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(service.getPaged(filter, pageable));
    }

    @Operation(summary = "Buscar unidade por ID", description = "Detalhes de uma unidade")
    @ApiResponse(responseCode = "200", description = "Unidade encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<UnidadeDTO> buscarPorId(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
