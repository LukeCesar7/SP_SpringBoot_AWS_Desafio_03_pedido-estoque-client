package com.github.LukeCesar7.mspedido.application;

import com.github.LukeCesar7.mspedido.application.representation.*;
import com.github.LukeCesar7.mspedido.domain.*;
import com.github.LukeCesar7.mspedido.infra.repository.ClientePedidoRepository;
import com.github.LukeCesar7.mspedido.infra.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.modelmapper.ModelMapper;

import javax.management.modelmbean.ModelMBean;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClientePedidoRepository clientePedidoRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;

    @Value("${endpoint.cliente}")
    private String endpointCliente;

    @Value("${endpoint.estoque}")
    private String endpointEstoque;

    @Transactional
    public PedidoResponse save(PedidoSaveRequest pedidoSaveRequest) throws Exception {

        String numeroPedido = UUID.randomUUID().toString();

        ClientePedido cliente = getCliente(pedidoSaveRequest.getCpf());
        if (cliente == null) {
            throw new IllegalArgumentException("Nenhum Cliente encontrado com o CPF informado.");
        }

        Pedido pedido = Pedido.builder()
                .numeroPedido(numeroPedido)
                .status(StatusPedido.PROCESSANDO)
                .cliente(cliente)
                .build();

        List<ProdutoRequest> produtoRequestList = pedidoSaveRequest.getProdutosRequest();
        List<Long> produtoIdsList = produtoRequestList.stream().map(ProdutoRequest::getProdutoId).toList();

        List<ProdutoResponse> disponibilidadeProdutos = consultarEstoque(produtoIdsList);

        if (!verificarEstoque(produtoRequestList, disponibilidadeProdutos)) {
            throw new Exception("Produtos fora de estoque. Por favor, tente novamente mais tarde.");
        }

        List<PedidoProduto> listaProdutos = pegarListaDeProdutos(produtoRequestList, disponibilidadeProdutos, pedido);
        pedido.setProdutos(listaProdutos);

        List<ProdutoRequest> atualizarEstoqueLista = produtoRequestList.stream().map(produtoRequest -> this.modelMapper.map(produtoRequest, ProdutoRequest.class)).toList();

        AtualizarEstoqueRequest atualizarEstoqueRequest = AtualizarEstoqueRequest.builder()
                .produtoRequestList(atualizarEstoqueLista)
                .build();

        chamadaRestAtualizarEstoque(atualizarEstoqueRequest);

        clientePedidoRepository.save(cliente);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        PedidoResponse pedidoResponse = modelMapper.map(pedidoSalvo, PedidoResponse.class);
        pedidoResponse.setCpf(cliente.getCpf());

        return pedidoResponse;
    }

public ClientePedido getCliente(String cpf) {
        return restTemplate.getForObject(endpointCliente + "?cpf=" + cpf, ClientePedido.class);
    }

    public List<ProdutoResponse> consultarEstoque(List<Long> produtoIds) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endpointEstoque);
        produtoIds.forEach(id -> builder.queryParam("produtoIds", id));

        ResponseEntity<List<ProdutoResponse>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Erro ao consultar o serviço de estoque. Código: {}", response.getStatusCode());
            throw new RuntimeException("Erro ao consultar o serviço de estoque.");
        }

        return response.getBody();
    }
    public void chamadaRestAtualizarEstoque(AtualizarEstoqueRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AtualizarEstoqueRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                endpointEstoque,
                HttpMethod.PUT,
                entity,
                Void.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Inventário atualizado com sucesso!");
        } else {
            System.err.println("Falha ao atualizar o inventário. Status: " + response.getStatusCode());

        }
    }

    private static HttpEntity<?> getHeaders() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(headers);
    }

    public static boolean verificarEstoque(List<ProdutoRequest> pedido, List<ProdutoResponse> estoque) {
        Map<Long, ProdutoResponse> estoqueMap = new HashMap<>();
        estoque.forEach(item -> estoqueMap.put(item.getId(), item));

        for (ProdutoRequest itemPedido : pedido) {
            ProdutoResponse itemEstoque = estoqueMap.get(itemPedido.getProdutoId());
            if (itemEstoque == null || itemEstoque.getQuantidade() < itemPedido.getQuantidade()) {
                log.warn("Estoque insuficiente para o produto ID {}.", itemPedido.getProdutoId());
                return false; // Regra: Pedido não pode ser realizado sem estoque suficiente.
            }
        }
        return true;
    }
    private List<PedidoProduto> pegarListaDeProdutos(List<ProdutoRequest> produtoRequests, 
                                                     List<ProdutoResponse> produtoResponses, Pedido pedido) {
        List<PedidoProduto> pedidoProdutos = new ArrayList<>();
        Map<Long, ProdutoResponse> responseMap = new HashMap<>();
        produtoResponses.forEach(produto -> responseMap.put(produto.getId(), produto));

        for (ProdutoRequest produtoRequest : produtoRequests) {
            ProdutoResponse produtoResponse = responseMap.get(produtoRequest.getProdutoId());
            if (produtoResponse != null && produtoRequest.getQuantidade() <= produtoResponse.getQuantidade()) {
                PedidoProduto pedidoProduto = PedidoProduto.builder()
                        .produtoId(produtoRequest.getProdutoId())
                        .produtoNome(produtoRequest.getNome())
                        .quantidade(produtoRequest.getQuantidade())

                        .pedido(pedido)
                        .build();
                pedidoProdutos.add(pedidoProduto);
            }
        }
        return pedidoProdutos;
    }
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Pedido pegarPedidoPeloNumero(String numeroPedido){
        return pedidoRepository.findByNumeroPedido(numeroPedido);
    }
}
