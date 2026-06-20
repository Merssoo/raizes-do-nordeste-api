package com.raizesdonordeste.api.controller;
import com.raizesdonordeste.api.dto.DTO.PedidoDTO;
import com.raizesdonordeste.api.dto.request.PedidoRequest;
import com.raizesdonordeste.api.dto.request.StatusPedidoRequest;
import com.raizesdonordeste.application.service.PedidoService;
import com.raizesdonordeste.domain.entity.Usuario;
import com.raizesdonordeste.domain.enums.CanalPedido;
import com.raizesdonordeste.domain.enums.StatusPedido;
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
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoDTO> criar(@RequestBody @Valid PedidoRequest request,
                                           @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.status(201).body(pedidoService.criarPedido(request, usuario));
    }

    @GetMapping
    public ResponseEntity<Page<PedidoDTO>> listar(
            @RequestParam(required = false) CanalPedido canalPedido,
            @RequestParam(required = false) StatusPedido status,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(pedidoService.listar(canalPedido, status, pageable));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody @Valid StatusPedidoRequest request) {
        pedidoService.atualizarStatus(id, request.status());
        return ResponseEntity.ok().build();
    }
}
