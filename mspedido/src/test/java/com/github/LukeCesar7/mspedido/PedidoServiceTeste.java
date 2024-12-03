package com.github.LukeCesar7.mspedido;

import com.github.LukeCesar7.mspedido.application.PedidoService;
import com.github.LukeCesar7.mspedido.application.representation.*;
import com.github.LukeCesar7.mspedido.domain.*;
import com.github.LukeCesar7.mspedido.infra.repository.ClientePedidoRepository;
import com.github.LukeCesar7.mspedido.infra.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClientePedidoRepository clientePedidoRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ModelMapper modelMapper;

    private static final String ENDPOINT_CLIENTE = "http://localhost:8081/cliente-service/api/clientes";
    private static final String ENDPOINT_ESTOQUE = "http://localhost:8082/estoque-service/api/estoque";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testSavePedido_FalhaClienteNaoEncontrado() {

        PedidoSaveRequest pedidoSaveRequest = new PedidoSaveRequest();
        pedidoSaveRequest.setCpf("12345678901");
        pedidoSaveRequest.setProdutosRequest(List.of(new ProdutoRequest(1L, "Produto Teste", 5)));

        when(restTemplate.getForObject(anyString(), eq(ClientePedido.class))).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pedidoService.save(pedidoSaveRequest);
        });

        assertEquals("Nenhum Cliente encontrado com o CPF informado.", exception.getMessage());
    }

    @Test
    void testConsultarEstoque_FalhaAoConsultar() {

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(),
                ArgumentMatchers.<ParameterizedTypeReference<List<ProdutoResponse>>>any()))
                .thenThrow(new RuntimeException("Erro ao consultar estoque"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.consultarEstoque(List.of(1L, 2L));
        });

        assertEquals("Erro ao consultar o serviço de estoque.", exception.getMessage());
    }

    @Test
    void testPegarPedidoPeloNumero_Sucesso() {
        Pedido pedido = new Pedido();
        pedido.setNumeroPedido("12345");
        when(pedidoRepository.findByNumeroPedido(anyString())).thenReturn(pedido);

        Pedido resultado = pedidoService.pegarPedidoPeloNumero("12345");

        assertNotNull(resultado);
        assertEquals("12345", resultado.getNumeroPedido());
        verify(pedidoRepository, times(1)).findByNumeroPedido(anyString());
    }
}

