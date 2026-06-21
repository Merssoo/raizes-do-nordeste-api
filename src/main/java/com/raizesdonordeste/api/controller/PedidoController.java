package com.raizesdonordeste.api.controller;
import com.raizesdonordeste.api.dto.AuthenticatedUsuarioDTO;
import com.raizesdonordeste.api.dto.PedidoDTO;
import com.raizesdonordeste.api.dto.request.PedidoRequest;
import com.raizesdonordeste.api.dto.request.StatusPedidoRequest;
import com.raizesdonordeste.api.exception.BusinessException;
import com.raizesdonordeste.application.service.PedidoService;
import com.raizesdonordeste.domain.entity.Usuario;
import com.raizesdonordeste.domain.enums.CanalPedido;
import com.raizesdonordeste.domain.enums.StatusPedido;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Pedidos", description = "Endpoints para gerenciamento de pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @Operation(summary = "Criar novo pedido", description = "Registra um pedido e inicia integração com pagamento")
    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso")
    @PostMapping
    public ResponseEntity<PedidoDTO> criar(@RequestBody @Valid PedidoRequest request,
                                           @AuthenticationPrincipal AuthenticatedUsuarioDTO usuarioDTO,
                                           @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {

        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new BusinessException("O cabeçalho Idempotency-Key é obrigatório.");
        }
        return ResponseEntity.status(201).body(pedidoService.criarPedido(request, usuarioDTO, idempotencyKey));
    }

    @Operation(summary = "Listar pedidos", description = "Lista ou filtra pedidos por canal e status")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos")
    @GetMapping
    public ResponseEntity<Page<PedidoDTO>> listar(
            @RequestParam(required = false) CanalPedido canalPedido,
            @RequestParam(required = false) StatusPedido status,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(pedidoService.listar(canalPedido, status, pageable));
    }

    @Operation(summary = "Atualizar status", description = "Atualiza o status de um pedido específico")
    @ApiResponse(responseCode = "200", description = "Status atualizado")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody @Valid StatusPedidoRequest request) {
        pedidoService.atualizarStatus(id, request.status());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Cancelar pedido", description = "Cancela um pedido existente")
    @ApiResponse(responseCode = "204", description = "Pedido cancelado")
    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        pedidoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
