package com.raizesdonordeste.application.service;
import com.raizesdonordeste.api.dto.PagamentoDTO;
import com.raizesdonordeste.api.dto.PedidoDTO;
import com.raizesdonordeste.api.dto.request.PagamentoRequest;
import com.raizesdonordeste.domain.entity.*;
import com.raizesdonordeste.domain.enums.StatusPedido;
import com.raizesdonordeste.infrastructure.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class PagamentoService extends BaseService<Pagamento, PagamentoDTO, Long> {

    private final PedidoRepository pedidoRepository;
    private final PedidoService pedidoService;

    public PagamentoService(PagamentoRepository repository,
                            PedidoRepository pedidoRepository,
                            PedidoService pedidoService) {
        super(repository, Pagamento.class);
        this.pedidoRepository = pedidoRepository;
        this.pedidoService = pedidoService;
    }

    @Override
    public Pagamento toEntity(PagamentoDTO dto) {
        Pedido pedido = dto.getPedidoDTO() != null ? pedidoRepository.findById(dto.getPedidoDTO().getId()).orElse(null) : null;
        return new Pagamento(dto.getId(), dto.getValor(), dto.getStatus(), dto.getFormaPagamento(), dto.getDataPagamento(), pedido);
    }

    @Override
    public PagamentoDTO toDto(Pagamento entity) {
        PedidoDTO pedidoDTO = pedidoService.toDto(entity.getPedido());
        return new PagamentoDTO(
                entity.getId(),
                entity.getValor(),
                entity.getStatus(),
                entity.getFormaPagamento(),
                entity.getDataPagamento(),
                pedidoDTO
        );
    }


    @Transactional
    public Map<String, String> processarPagamento(PagamentoRequest request) {
        PedidoDTO pedidoDTO = pedidoService.getByIdOrError(request.pedidoId());

        boolean aprovado = Math.random() > 0.3;

        PagamentoDTO pagamentoDTO = new PagamentoDTO();
        pagamentoDTO.setPedidoDTO(pedidoDTO);
        pagamentoDTO.setValor(pedidoDTO.getValorTotal());
        pagamentoDTO.setDataPagamento(LocalDateTime.now());
        pagamentoDTO.setFormaPagamento(request.formaPagamento());

        if (aprovado) {
            pagamentoDTO.setStatus("APROVADO");
            pedidoDTO.setStatus(StatusPedido.PAGO);
            pedidoService.save(pedidoDTO);
            this.save(pagamentoDTO);
            return Map.of("status", "APROVADO");
        } else {
            pagamentoDTO.setStatus("RECUSADO");
            pedidoDTO.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
            pedidoService.save(pedidoDTO);
            this.save(pagamentoDTO);
            return Map.of("status", "RECUSADO");
        }
    }
}
