package com.raizesdonordeste.api.controller;
import com.raizesdonordeste.api.dto.request.PagamentoRequest;
import com.raizesdonordeste.application.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Pagamentos", description = "Endpoints para processamento de pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @Operation(summary = "Processar pagamento", description = "Simula o processamento de um pagamento para um pedido")
    @ApiResponse(responseCode = "200", description = "Pagamento processado")
    @PostMapping
    public ResponseEntity<Map<String, String>> processar(@RequestBody @Valid PagamentoRequest request) {
        return ResponseEntity.ok(pagamentoService.processarPagamento(request));
    }
}
