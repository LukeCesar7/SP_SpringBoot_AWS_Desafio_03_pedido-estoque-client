package com.github.LukeCesar7.msestoque;

import com.github.LukeCesar7.msestoque.application.EstoqueRepository;
import com.github.LukeCesar7.msestoque.application.EstoqueService;
import com.github.LukeCesar7.msestoque.domain.Produto;
import com.github.LukeCesar7.msestoque.representation.AtualizarEstoqueRequest;
import com.github.LukeCesar7.msestoque.representation.ProdutoRequest;
import com.github.LukeCesar7.msestoque.representation.ProdutoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstoqueServiceTest {

    @Mock
    private EstoqueRepository estoqueRepository;

    @Mock
    private AtualizarEstoqueRequest atualizarEstoqueRequest;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EstoqueService estoqueService;

    private Produto produto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        produto = new Produto(1L, "Produto Teste", 10);
    }

    @Test
    void testSave() {
        when(estoqueRepository.save(produto)).thenReturn(produto);
        Produto result = estoqueService.save(produto);
        assertEquals(produto, result);
        verify(estoqueRepository, times(1)).save(produto);
    }

    @Test
    void testFindById() {
        when(estoqueRepository.findById(1L)).thenReturn(Optional.of(produto));
        Produto result = estoqueService.findById(1L);
        assertEquals(produto, result);
        verify(estoqueRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(estoqueRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> estoqueService.findById(1L));
    }

    @Test
    void testFindAll() {
        List<Produto> produtos = List.of(produto);
        when(estoqueRepository.findAll()).thenReturn(produtos);
        List<Produto> result = estoqueService.findAll();
        assertEquals(produtos, result);
        verify(estoqueRepository, times(1)).findAll();
    }

    @Test
    void testChecarDisponibilidade() {
        List<Long> ids = List.of(1L);
        ProdutoResponse produtoResponse = new ProdutoResponse(1L, "Produto Teste", 10);

        when(estoqueRepository.findProdutoByIdIn(ids)).thenReturn(List.of(produto));
        when(modelMapper.map(produto, ProdutoResponse.class)).thenReturn(produtoResponse);

        List<ProdutoResponse> result = estoqueService.checarDisponibilidade(ids);

        assertEquals(1, result.size());
        assertEquals(produtoResponse, result.get(0));
        verify(modelMapper, times(1)).map(produto, ProdutoResponse.class);
    }


    @Test
    void testAtualizarQte_NotFound() {

        Produto produtoMock = new Produto(1L, 10);


        when(estoqueRepository.findProdutoByIdIn(Collections.singletonList(1L)))
                .thenReturn(Collections.singletonList(produtoMock));


        List<ProdutoRequest> produtoRequests = new ArrayList<>();
        ProdutoRequest produtoRequest = new ProdutoRequest(1L, 100);
        produtoRequests.add(produtoRequest);


        AtualizarEstoqueRequest atualizarEstoqueRequest = AtualizarEstoqueRequest.builder()
                .produtoRequestList(produtoRequests)
                .build();

        assertThrows(EstoqueService.ResourceNotFoundException.class, () ->
                estoqueService.atualizarEstoque(atualizarEstoqueRequest));


        verify(estoqueRepository).findProdutoByIdIn(Collections.singletonList(1L));
    }

    @Test
    void testAtualizarQte_QuantidadeAlteradaCorretamente() {
        // Lista de produtos mock com quantidades iniciais
        List<Produto> produtosMock = Arrays.asList(
                new Produto(1L, 20),
                new Produto(2L, 30)
        );


        when(estoqueRepository.findProdutoByIdIn(anyList())) // Aceita qualquer lista de IDs
                .thenReturn(produtosMock);


        List<ProdutoRequest> produtoRequests = Arrays.asList(
                new ProdutoRequest(1L, 5),
                new ProdutoRequest(2L, 10)
        );


        AtualizarEstoqueRequest atualizarEstoqueRequest = AtualizarEstoqueRequest.builder()
                .produtoRequestList(produtoRequests)
                .build();


        estoqueService.atualizarEstoque(atualizarEstoqueRequest);


        assertEquals(15, produtosMock.get(0).getQuantidade()); // 20 - 5 = 15
        assertEquals(20, produtosMock.get(1).getQuantidade()); // 30 - 10 = 20
    }


    @Test
    void testRemoverProduto_Sucesso() {
        Long produtoId = 1L;
        Produto produto = new Produto(produtoId, "Produto Teste", 10);

        when(estoqueRepository.findById(produtoId)).thenReturn(Optional.of(produto));

        estoqueService.removerProduto(produtoId);

        verify(estoqueRepository, times(1)).delete(produto);
    }

    @Test
    void testRemoverProduto_ProdutoNaoEncontrado() {
        Long produtoId = 1L;

        when(estoqueRepository.findById(produtoId)).thenReturn(Optional.empty());

        assertThrows(EstoqueService.ResourceNotFoundException.class, () -> estoqueService.removerProduto(produtoId));
        verify(estoqueRepository, never()).delete(any());
    }
}

