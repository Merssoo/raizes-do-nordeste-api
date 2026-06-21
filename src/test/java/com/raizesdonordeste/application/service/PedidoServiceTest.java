package com.raizesdonordeste.application.service;

import com.raizesdonordeste.api.dto.PedidoDTO;
import com.raizesdonordeste.api.dto.request.PedidoRequest;
import com.raizesdonordeste.domain.entity.Pedido;
import com.raizesdonordeste.infrastructure.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void save_DeveRetornarPedidoSalvo() {
        PedidoDTO dto = new PedidoDTO();
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(new Pedido());
        
        PedidoDTO resultado = pedidoService.save(dto);
        assertNotNull(resultado);
    }
}
