package com.raizesdonordeste.application.service;
import com.raizesdonordeste.api.dto.request.PagamentoRequest;
import com.raizesdonordeste.api.exception.BusinessException;
import com.raizesdonordeste.domain.entity.Pagamento;
import com.raizesdonordeste.domain.entity.Pedido;
import com.raizesdonordeste.domain.enums.StatusPedido;
import com.raizesdonordeste.infrastructure.repository.PagamentoRepository;
import com.raizesdonordeste.infrastructure.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final PedidoRepository pedidoRepository;

    @Transactional
    public Map<String, String> processarPagamento(PagamentoRequest request) {

        Pedido pedido = pedidoRepository.findById(request.pedidoId())
                .orElseThrow(() -> new BusinessException("Pedido não encontrado"));

        boolean aprovado = Math.random() > 0.3;

        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamento.setValor(pedido.getValorTotal());
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setFormaPagamento(request.formaPagamento());

        if (aprovado) {
            pagamento.setStatus("APROVADO");
            pedido.setStatus(StatusPedido.PAGO);
            pedidoRepository.save(pedido);
            pagamentoRepository.save(pagamento);
            return Map.of("status", "APROVADO");
        } else {
            pagamento.setStatus("RECUSADO");
            pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
            pedidoRepository.save(pedido);
            pagamentoRepository.save(pagamento);
            return Map.of("status", "RECUSADO");
        }
    }
}
