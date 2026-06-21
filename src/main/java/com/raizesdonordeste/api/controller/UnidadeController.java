package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.api.dto.EstoqueDTO;
import com.raizesdonordeste.api.dto.UnidadeDTO;
import com.raizesdonordeste.application.service.UnidadeService;
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
public class UnidadeController {

    private final UnidadeService service;

    @PostMapping
    public ResponseEntity<UnidadeDTO> criar(@RequestBody @Valid UnidadeDTO unidadeDto) {
        return ResponseEntity.status(201).body(service.criar(unidadeDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnidadeDTO> atualizar(@PathVariable Long id, @RequestBody @Valid UnidadeDTO unidadeDto) {
        return ResponseEntity.ok(service.atualizar(id, unidadeDto));
    }

    @GetMapping
    public ResponseEntity<Page<UnidadeDTO>> listarTodos(
            @RequestParam(required = false) String filter,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(service.getPaged(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnidadeDTO> buscarPorId(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
