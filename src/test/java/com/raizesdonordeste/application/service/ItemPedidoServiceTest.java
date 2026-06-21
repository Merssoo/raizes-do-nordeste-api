package com.raizesdonordeste.application.service;

import com.raizesdonordeste.api.dto.ItemPedidoDTO;
import com.raizesdonordeste.domain.entity.ItemPedido;
import com.raizesdonordeste.domain.entity.Pedido;
import com.raizesdonordeste.domain.entity.Produto;
import com.raizesdonordeste.infrastructure.repository.ItemPedidoRepository;
import com.raizesdonordeste.infrastructure.repository.PedidoRepository;
import com.raizesdonordeste.infrastructure.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemPedidoServiceTest {

    @Mock
    private ItemPedidoRepository itemPedidoRepository;
    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private PedidoService pedidoService;
    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private ItemPedidoService itemPedidoService;

    @Test
    void toEntity_ValidDto_ReturnsEntity() {
        ItemPedidoDTO dto = new ItemPedidoDTO(1L, 2, new BigDecimal("10.00"), new BigDecimal("20.00"), 1L, 1L);
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        Produto produto = new Produto();
        produto.setId(1L);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        ItemPedido entity = itemPedidoService.toEntity(dto);

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getQuantidade(), entity.getQuantidade());
        assertEquals(pedido, entity.getPedido());
        assertEquals(produto, entity.getProduto());
    }

    @Test
    void toDto_ValidEntity_ReturnsDto() {
        ItemPedido entity = new ItemPedido();
        entity.setId(1L);
        entity.setQuantidade(2);
        entity.setPrecoUnitario(new BigDecimal("10.00"));
        entity.setSubtotal(new BigDecimal("20.00"));
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        entity.setPedido(pedido);
        Produto produto = new Produto();
        produto.setId(1L);
        entity.setProduto(produto);

        ItemPedidoDTO dto = itemPedidoService.toDto(entity);

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getQuantidade(), dto.getQuantidade());
        assertEquals(entity.getPedido().getId(), dto.getPedidoId());
        assertEquals(entity.getProduto().getId(), dto.getProdutoId());
    }
}
