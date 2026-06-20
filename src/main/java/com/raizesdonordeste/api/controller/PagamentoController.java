package com.raizesdonordeste.api.controller;
import com.raizesdonordeste.api.dto.request.PagamentoRequest;
import com.raizesdonordeste.application.service.PagamentoService;
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
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<Map<String, String>> processar(@RequestBody @Valid PagamentoRequest request) {
        return ResponseEntity.ok(pagamentoService.processarPagamento(request));
    }
}
