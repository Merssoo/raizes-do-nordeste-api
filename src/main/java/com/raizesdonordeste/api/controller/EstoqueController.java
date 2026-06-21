package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.api.dto.EstoqueDTO;
import com.raizesdonordeste.api.dto.ProdutoDTO;
import com.raizesdonordeste.application.service.EstoqueService;
import com.raizesdonordeste.application.service.ProdutoService;
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
@RequestMapping("/estoques")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Estoques e Produtos", description = "Endpoints para gerenciamento de estoque e catálogo de produtos")
public class EstoqueController {

    private final EstoqueService service;
    private final ProdutoService produtoService;

    @Operation(summary = "Criar estoque", description = "Adiciona produto ao estoque de uma unidade")
    @ApiResponse(responseCode = "201", description = "Estoque criado")
    @PostMapping
    public ResponseEntity<EstoqueDTO> criar(@RequestBody @Valid EstoqueDTO estoqueDto) {
        return ResponseEntity.status(201).body(service.criar(estoqueDto));
    }

    @Operation(summary = "Atualizar estoque", description = "Atualiza quantidade de estoque")
    @ApiResponse(responseCode = "200", description = "Estoque atualizado")
    @PutMapping("/{id}")
    public ResponseEntity<EstoqueDTO> atualizar(@PathVariable Long id, @RequestBody @Valid EstoqueDTO estoqueDto) {
        return ResponseEntity.ok(service.atualizar(id, estoqueDto));
    }

    @Operation(summary = "Listar estoques por unidade", description = "Lista estoques de uma unidade específica")
    @ApiResponse(responseCode = "200", description = "Lista de estoques")
    @GetMapping("/unidade/{unidadeId}")
    public ResponseEntity<Page<EstoqueDTO>> listarPorUnidade(
            @PathVariable Long unidadeId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.listarPorUnidade(unidadeId, pageable));
    }

    @Operation(summary = "Listar todos estoques", description = "Lista todos os estoques da rede")
    @ApiResponse(responseCode = "200", description = "Lista de estoques")
    @GetMapping
    public ResponseEntity<Page<EstoqueDTO>> listarTodos(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.getPaged(pageable));
    }

    @Operation(summary = "Buscar estoque por ID", description = "Detalhes de um estoque")
    @ApiResponse(responseCode = "200", description = "Estoque encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<EstoqueDTO> buscarPorId(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualizar produto", description = "Atualiza dados de um produto")
    @ApiResponse(responseCode = "200", description = "Produto atualizado")
    @PutMapping("/produtos/{id}")
    public ResponseEntity<ProdutoDTO> atualizarProduto(@PathVariable Long id, @RequestBody @Valid ProdutoDTO produtoDto) {
        return ResponseEntity.ok(produtoService.atualizar(id, produtoDto));
    }

    @Operation(summary = "Desativar produto", description = "Inativa um produto do catálogo")
    @ApiResponse(responseCode = "204", description = "Produto inativado")
    @PutMapping("/produtos/desativar/{id}")
    public ResponseEntity<Void> inativarProduto(@PathVariable Long id) {
        produtoService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Criar produto", description = "Adiciona novo produto ao catálogo")
    @ApiResponse(responseCode = "201", description = "Produto criado")
    @PostMapping("/produtos")
    public ResponseEntity<ProdutoDTO> criarProduto(@RequestBody @Valid ProdutoDTO produtoDto) {
        return ResponseEntity.status(201).body(produtoService.criar(produtoDto));
    }

    @Operation(summary = "Listar produtos por unidade", description = "Lista produtos com saldo em uma unidade")
    @ApiResponse(responseCode = "200", description = "Lista de produtos")
    @GetMapping("/unidade/{idUnidade}/produtos")
    public ResponseEntity<Page<ProdutoDTO>> listarProdutosPorUnidadeComSaldo(
            @PathVariable Long idUnidade,
            @RequestParam(required = false) String filter,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(produtoService.getProdutosPorUnidadeComSaldo(idUnidade, filter, pageable));
    }

    @Operation(summary = "Buscar produto por ID", description = "Detalhes de um produto")
    @ApiResponse(responseCode = "200", description = "Produto encontrado")
    @GetMapping("/produtos/{id}")
    public ResponseEntity<ProdutoDTO> buscarProdutoPorId(@PathVariable Long id) {
        return produtoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar todos produtos", description = "Lista todos produtos do catálogo")
    @ApiResponse(responseCode = "200", description = "Lista de produtos")
    @GetMapping("/produtos")
    public ResponseEntity<Page<ProdutoDTO>> listarTodosProdutos(
            @RequestParam(required = false) String filter,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(produtoService.getPaged(filter, pageable));
    }
}
