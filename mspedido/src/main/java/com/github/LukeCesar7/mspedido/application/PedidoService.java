package com.github.LukeCesar7.mspedido.application;

import com.github.LukeCesar7.mspedido.domain.ClientePedido;
import com.github.LukeCesar7.mspedido.domain.Pedido;
import com.github.LukeCesar7.mspedido.infra.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    @Value("${endpoint.cliente}")
    private String endpoint;

    @Transactional
    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public ClientePedido getCliente(String cpf){
        RestTemplate rest = new RestTemplate();
        return rest.getForObject(endpoint +"?cpf=" + cpf, ClientePedido.class);
    }


}
