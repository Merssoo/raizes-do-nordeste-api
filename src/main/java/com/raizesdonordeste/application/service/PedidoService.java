package com.raizesdonordeste.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import com.raizesdonordeste.api.dto.*;
import com.raizesdonordeste.api.dto.request.ItemPedidoRequest;
import com.raizesdonordeste.api.dto.request.PedidoRequest;
import com.raizesdonordeste.api.exception.BusinessException;
import com.raizesdonordeste.api.exception.GlobalExceptionHandler;
import com.raizesdonordeste.domain.entity.*;
import com.raizesdonordeste.domain.enums.CanalPedido;
import com.raizesdonordeste.domain.enums.RoleEnum;
import com.raizesdonordeste.domain.enums.StatusPedido;
import com.raizesdonordeste.infrastructure.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService extends BaseService<Pedido, PedidoDTO, Long> {

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    private final UnidadeRepository unidadeRepository;
    private final UsuarioRepository usuarioRepository;
    private final UnidadeService unidadeService;
    private final EstoqueService estoqueService;
    private final ProdutoService produtoService;
    private final ItemPedidoService itemPedidoService;
    private final IdempotencyRepository idempotencyRepository;
    private final ObjectMapper objectMapper;

    public PedidoService(PedidoRepository repository,
                         UnidadeRepository unidadeRepository,
                         UsuarioRepository usuarioRepository,
                         UnidadeService unidadeService,
                         EstoqueService estoqueService,
                         ProdutoService produtoService,
                         @Lazy ItemPedidoService itemPedidoService,
                         IdempotencyRepository idempotencyRepository,
                         ObjectMapper objectMapper) {
        super(repository, Pedido.class);
        this.unidadeRepository = unidadeRepository;
        this.usuarioRepository = usuarioRepository;
        this.unidadeService = unidadeService;
        this.estoqueService = estoqueService;
        this.produtoService = produtoService;
        this.itemPedidoService = itemPedidoService;
        this.idempotencyRepository = idempotencyRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Pedido toEntity(PedidoDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setId(dto.getId());
        pedido.setValorTotal(dto.getValorTotal());
        pedido.setCanalPedido(dto.getCanalPedido());
        pedido.setStatus(dto.getStatus());
        pedido.setCreatedAt(dto.getCreatedAt());
        if (dto.getClienteId() != null) {
            pedido.setCliente(usuarioRepository.findById(dto.getClienteId()).orElse(null));
        }
        if (dto.getUnidadeId() != null) {
            pedido.setUnidade(unidadeRepository.findById(dto.getUnidadeId()).orElse(null));
        }
        return pedido;
    }

    @Override
    public PedidoDTO toDto(Pedido entity) {
        return new PedidoDTO(
                entity.getId(),
                entity.getValorTotal(),
                entity.getCanalPedido(),
                entity.getStatus(),
                entity.getCliente() != null ? entity.getCliente().getId() : null,
                entity.getUnidade() != null ? entity.getUnidade().getId() : null,
                entity.getCreatedAt()
        );
    }

    @Transactional
    public PedidoDTO criarPedido(PedidoRequest request, AuthenticatedUsuarioDTO usuarioDTO, String idempotencyKey) {
        if (idempotencyKey != null && idempotencyRepository.existsById(idempotencyKey)) {
            try {
                String responseBody = idempotencyRepository.findById(idempotencyKey).get().getResponseBody();
                return objectMapper.readValue(responseBody, PedidoDTO.class);
            } catch (Exception e) {
                throw new BusinessException("Erro ao recuperar resposta idempotente");
            }
        }

        Long idCliente = usuarioDTO.id();

        if (!usuarioDTO.role().equals(RoleEnum.CLIENTE.name())) {
            if (request.idCliente() == null) {
                throw new BusinessException("ID do cliente é obrigatório para usuários não clientes");
            }
            idCliente = usuarioRepository.findById(request.idCliente()).orElseThrow(() -> new BusinessException("Cliente não encontrado")).getId();
        }

        UnidadeDTO unidadeDTO = unidadeService.getByIdOrError(request.unidadeId());

        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setUnidadeId(unidadeDTO.getId());
        pedidoDTO.setClienteId(idCliente);
        pedidoDTO.setCanalPedido(request.canalPedido());
        pedidoDTO.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
        pedidoDTO.setCreatedAt(LocalDateTime.now());
        BigDecimal valorTotal = BigDecimal.ZERO;

        List<ItemPedidoDTO> itens = new ArrayList<>();
        List<Long> idsProduto = request.itens().stream().map(ItemPedidoRequest::produtoId).collect(Collectors.toList());

        QEstoque qEstoque = QEstoque.estoque;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qEstoque.produto.id.in(idsProduto));
        builder.and(qEstoque.unidade.id.eq(unidadeDTO.getId()));

        List<EstoqueDTO> estoqueDTOS = estoqueService.getAllByPredicate(builder);

        for (ItemPedidoRequest itemReq : request.itens()) {
            ProdutoDTO produtoDTO = produtoService.getByIdOrError(itemReq.produtoId());

            EstoqueDTO estoqueDTO = estoqueDTOS.stream()
                    .filter(e -> e.getProdutoId().equals(produtoDTO.getId()))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException("Produto não disponível nesta unidade: " + produtoDTO.getNome()));

            if (estoqueDTO.getQuantidade() < itemReq.quantidade()) {
                throw new BusinessException("Estoque insuficiente para o produto: " + produtoDTO.getNome());
            }

            estoqueDTO.setQuantidade(estoqueDTO.getQuantidade() - itemReq.quantidade());
            estoqueService.save(estoqueDTO);

            ItemPedidoDTO itemPedidoDTO = new ItemPedidoDTO();
            itemPedidoDTO.setProdutoId(produtoDTO.getId());
            itemPedidoDTO.setQuantidade(itemReq.quantidade());
            itemPedidoDTO.setPrecoUnitario(produtoDTO.getPreco());
            itemPedidoDTO.setSubtotal(produtoDTO.getPreco().multiply(BigDecimal.valueOf(itemReq.quantidade())));
            valorTotal = valorTotal.add(itemPedidoDTO.getSubtotal());
            itens.add(itemPedidoDTO);
        }

        pedidoDTO.setValorTotal(valorTotal);
        PedidoDTO pedidoSalvoDTO = this.save(pedidoDTO);

        for (ItemPedidoDTO itemPedidoDTO : itens) {
            itemPedidoDTO.setPedidoId(pedidoSalvoDTO.getId());
            itemPedidoService.save(itemPedidoDTO);
        }

        if (idempotencyKey != null) {
            try {
                String responseBody = objectMapper.writeValueAsString(pedidoSalvoDTO);
                idempotencyRepository.save(new IdempotencyKey(idempotencyKey, responseBody, LocalDateTime.now()));
            } catch (Exception e) {
                logger.error("Erro ao salvar resposta idempotente", e);
            }
        }
        return pedidoSalvoDTO;
    }

    @Transactional(readOnly = true)
    public Page<PedidoDTO> listar(CanalPedido canalPedido, StatusPedido status, Pageable pageable) {
        QPedido qPedido = QPedido.pedido;
        BooleanBuilder builder = new BooleanBuilder();
        if (canalPedido != null) builder.and(qPedido.canalPedido.eq(canalPedido));
        if (status != null) builder.and(qPedido.status.eq(status));

        return this.getPaged(builder, pageable);
    }

    @Transactional
    public void cancelar(Long id) {
        PedidoDTO dto = getByIdOrError(id);

        if (dto.getStatus() == StatusPedido.ENTREGUE || dto.getStatus() == StatusPedido.CANCELADO) {
            throw new BusinessException("Pedido não pode ser cancelado neste estado");
        }

        List<ItemPedidoDTO> itens = itemPedidoService.getAllByPredicate(new BooleanBuilder(QItemPedido.itemPedido.pedido.id.eq(dto.getId())));

        List<Long> idsProduto = new ArrayList<>();
        itens.forEach(item -> idsProduto.add(item.getProdutoId()));

        List<EstoqueDTO> estoqueDTOS = estoqueService.getAllByPredicate(new BooleanBuilder(QEstoque.estoque.produto.id.in(idsProduto)
                .and(QEstoque.estoque.unidade.id.eq(dto.getUnidadeId()))));

        for (ItemPedidoDTO itemPedidoDTO : itens) {
            EstoqueDTO estoqueDTO = estoqueDTOS.stream().filter(e -> e.getProdutoId().equals(itemPedidoDTO.getProdutoId())).findFirst()
                    .orElseThrow(() -> new BusinessException("Estoque não encontrado para o produto: " + itemPedidoDTO.getProdutoId()));

            estoqueDTO.setQuantidade(estoqueDTO.getQuantidade() + itemPedidoDTO.getQuantidade());
            estoqueService.save(estoqueDTO);
        }

        dto.setStatus(StatusPedido.CANCELADO);
        save(dto);
    }

    @Transactional
    public void atualizarStatus(Long id, StatusPedido novoStatus) {
        PedidoDTO pedidoDTO = this.getByIdOrError(id);

        if (pedidoDTO.getStatus() == StatusPedido.ENTREGUE) {
            throw new BusinessException("Não é possível alterar status de pedido entregue");
        }

        pedidoDTO.setStatus(novoStatus);
        this.save(pedidoDTO);
    }
}
