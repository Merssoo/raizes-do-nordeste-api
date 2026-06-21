package com.raizesdonordeste.application.service;

import com.raizesdonordeste.api.dto.PedidoDTO;
import com.raizesdonordeste.api.dto.request.PagamentoRequest;
import com.raizesdonordeste.domain.entity.Pagamento;
import com.raizesdonordeste.infrastructure.repository.PagamentoRepository;
import com.raizesdonordeste.infrastructure.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;
    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PagamentoService pagamentoService;

    @Test
    void processarPagamentoShouldReturnStatus() {
        PagamentoRequest request = new PagamentoRequest(1L, "CREDITO");
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setId(1L);
        pedidoDTO.setValorTotal(BigDecimal.valueOf(100.0));

        when(pedidoService.getByIdOrError(1L)).thenReturn(pedidoDTO);
        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, String> result = pagamentoService.processarPagamento(request);

        assertNotNull(result);
        assertNotNull(result.get("status"));
    }
}
