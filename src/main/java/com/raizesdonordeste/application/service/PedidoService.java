package com.raizesdonordeste.application.service;

import com.querydsl.core.BooleanBuilder;
import com.raizesdonordeste.api.dto.DTO.PedidoDTO;
import com.raizesdonordeste.api.dto.request.ItemPedidoRequest;
import com.raizesdonordeste.api.dto.request.PedidoRequest;
import com.raizesdonordeste.api.exception.BusinessException;
import com.raizesdonordeste.domain.entity.*;
import com.raizesdonordeste.domain.enums.CanalPedido;
import com.raizesdonordeste.domain.enums.StatusPedido;
import com.raizesdonordeste.infrastructure.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService extends BaseService<Pedido, PedidoDTO, Long> {

    private final ItemPedidoRepository itemPedidoRepository;
    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;
    private final UnidadeRepository unidadeRepository;
    private final UsuarioRepository usuarioRepository;

    public PedidoService(PedidoRepository repository,
                         ItemPedidoRepository itemPedidoRepository,
                         EstoqueRepository estoqueRepository,
                         ProdutoRepository produtoRepository,
                         UnidadeRepository unidadeRepository,
                         UsuarioRepository usuarioRepository) {
        super(repository, Pedido.class);
        this.itemPedidoRepository = itemPedidoRepository;
        this.estoqueRepository = estoqueRepository;
        this.produtoRepository = produtoRepository;
        this.unidadeRepository = unidadeRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Pedido toEntity(PedidoDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setId(dto.getId());
        pedido.setValorTotal(dto.getValorTotal());
        pedido.setCanalPedido(dto.getCanalPedido());
        pedido.setStatus(dto.getStatus());
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
                entity.getUnidade() != null ? entity.getUnidade().getId() : null
        );
    }

    @Transactional
    public PedidoDTO criarPedido(PedidoRequest request, Usuario cliente) {
        Unidade unidade = unidadeRepository.findById(request.unidadeId())
                .orElseThrow(() -> new BusinessException("Unidade não encontrada"));

        Pedido pedido = new Pedido();
        pedido.setUnidade(unidade);
        pedido.setCliente(cliente);
        pedido.setCanalPedido(request.canalPedido());
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
        BigDecimal valorTotal = BigDecimal.ZERO;

        List<ItemPedido> itens = new ArrayList<>();
        List<Long> idsProduto = request.itens().stream().map(ItemPedidoRequest::produtoId).collect(Collectors.toList());

        QEstoque qEstoque = QEstoque.estoque;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qEstoque.produto.id.in(idsProduto));
        builder.and(qEstoque.unidade.id.eq(unidade.getId()));

        List<Estoque> estoques = (List<Estoque>) estoqueRepository.findAll(builder);

        for (ItemPedidoRequest itemReq : request.itens()) {
            Produto produto = produtoRepository.findById(itemReq.produtoId())
                    .orElseThrow(() -> new BusinessException("Produto não encontrado: " + itemReq.produtoId()));

            Estoque estoque = estoques.stream()
                    .filter(e -> e.getProduto().getId().equals(produto.getId()))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException("Produto não disponível nesta unidade: " + produto.getNome()));

            if (estoque.getQuantidade() < itemReq.quantidade()) {
                throw new BusinessException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            estoque.setQuantidade(estoque.getQuantidade() - itemReq.quantidade());
            estoqueRepository.save(estoque);

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemReq.quantidade());
            item.setPrecoUnitario(produto.getPreco());
            item.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(itemReq.quantidade())));
            valorTotal = valorTotal.add(item.getSubtotal());
            itens.add(item);
        }

        pedido.setValorTotal(valorTotal);
        Pedido pedidoSalvo = repository.save(pedido);

        for (ItemPedido item : itens) {
            itemPedidoRepository.save(item);
        }
        return toDto(pedidoSalvo);
    }

    @Transactional(readOnly = true)
    public Page<PedidoDTO> listar(CanalPedido canalPedido, StatusPedido status, Pageable pageable) {
        QPedido qPedido = QPedido.pedido;
        BooleanBuilder builder = new BooleanBuilder();
        if (canalPedido != null) builder.and(qPedido.canalPedido.eq(canalPedido));
        if (status != null) builder.and(qPedido.status.eq(status));

        return ((PedidoRepository) repository).findAll(builder, pageable).map(this::toDto);
    }

    @Transactional
    public void atualizarStatus(Long id, StatusPedido novoStatus) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Pedido não encontrado"));

        if (pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new BusinessException("Não é possível alterar status de pedido entregue");
        }

        pedido.setStatus(novoStatus);
        repository.save(pedido);
    }
}
