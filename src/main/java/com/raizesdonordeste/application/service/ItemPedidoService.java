package com.raizesdonordeste.application.service;

import com.raizesdonordeste.api.dto.ItemPedidoDTO;
import com.raizesdonordeste.api.dto.PedidoDTO;
import com.raizesdonordeste.api.dto.ProdutoDTO;
import com.raizesdonordeste.domain.entity.ItemPedido;
import com.raizesdonordeste.infrastructure.repository.ItemPedidoRepository;
import com.raizesdonordeste.infrastructure.repository.PedidoRepository;
import com.raizesdonordeste.infrastructure.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemPedidoService extends BaseService<ItemPedido, ItemPedidoDTO, Long> {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoService pedidoService;
    private final ProdutoService produtoService;

    public ItemPedidoService(ItemPedidoRepository repository,
                             PedidoRepository pedidoRepository,
                             ProdutoRepository produtoRepository,
                             PedidoService pedidoService,
                             ProdutoService produtoService) {
        super(repository, ItemPedido.class);
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.pedidoService = pedidoService;
        this.produtoService = produtoService;
    }

    @Override
    public ItemPedido toEntity(ItemPedidoDTO dto) {
        ItemPedido entity = new ItemPedido();
        entity.setId(dto.getId());
        entity.setQuantidade(dto.getQuantidade());
        entity.setPrecoUnitario(dto.getPrecoUnitario());
        entity.setSubtotal(dto.getSubtotal());
        entity.setPedido(dto.getPedidoDTO() != null ? pedidoRepository.findById(dto.getPedidoDTO().getId()).orElse(null) : null);
        entity.setProduto(dto.getProdutoDTO() != null ? produtoRepository.findById(dto.getProdutoDTO().getId()).orElse(null) : null);
        return entity;
    }

    @Override
    public ItemPedidoDTO toDto(ItemPedido entity) {
        PedidoDTO pedidoDTO = pedidoService.toDto(entity.getPedido());
        ProdutoDTO produtoDTO = produtoService.toDto(entity.getProduto());
        return new ItemPedidoDTO(
                entity.getId(),
                entity.getQuantidade(),
                entity.getPrecoUnitario(),
                entity.getSubtotal(),
                pedidoDTO,
                produtoDTO
        );
    }
}
