package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.api.dto.DTO.EstoqueDTO;
import com.raizesdonordeste.api.dto.DTO.ProdutoDTO;
import com.raizesdonordeste.application.service.EstoqueService;
import com.raizesdonordeste.application.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/estoques")
@RequiredArgsConstructor
public class EstoqueController {

    private final EstoqueService service;
    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<EstoqueDTO> criar(@RequestBody @Valid EstoqueDTO estoqueDto) {
        return ResponseEntity.status(201).body(service.save(estoqueDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstoqueDTO> atualizar(@PathVariable Long id, @RequestBody @Valid EstoqueDTO estoqueDto) {
        return ResponseEntity.ok(service.update(id, estoqueDto));
    }

    @GetMapping("/unidade/{unidadeId}")
    public ResponseEntity<List<EstoqueDTO>> listarPorUnidade(@PathVariable Long unidadeId) {
        return ResponseEntity.ok(service.listarPorUnidade(unidadeId));
    }

    @GetMapping
    public ResponseEntity<Page<EstoqueDTO>> listarTodos(
            @RequestParam(required = false) String filter,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.getPaged(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstoqueDTO> buscarPorId(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/produtos/{id}")
    public ResponseEntity<ProdutoDTO> atualizarProduto(@PathVariable Long id, @RequestBody @Valid ProdutoDTO produtoDto) {
        return ResponseEntity.ok(produtoService.update(id, produtoDto));
    }

    @DeleteMapping("/produtos/{id}")
    public ResponseEntity<Void> inativarProduto(@PathVariable Long id) {
        produtoService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/produtos")
    public ResponseEntity<ProdutoDTO> criarProduto(@RequestBody @Valid ProdutoDTO produtoDto) {
        return ResponseEntity.status(201).body(produtoService.save(produtoDto));
    }

    @GetMapping("/{idUnidade}/produtos")
    public ResponseEntity<Page<ProdutoDTO>> listarProdutosPorUnidade(
            @PathVariable Long idUnidade,
            @RequestParam(required = false) String filter,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(produtoService.getProdutosPorUnidade(idUnidade, filter, pageable));
    }

    @GetMapping("/produtos/{id}")
    public ResponseEntity<ProdutoDTO> buscarProdutoPorId(@PathVariable Long id) {
        return produtoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
