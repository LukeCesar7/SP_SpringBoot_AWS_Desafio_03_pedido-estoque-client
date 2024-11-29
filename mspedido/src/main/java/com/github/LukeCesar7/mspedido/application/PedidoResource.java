package com.github.LukeCesar7.mspedido.application;

import com.github.LukeCesar7.mspedido.application.representation.PedidoClienteRequest;
import com.github.LukeCesar7.mspedido.domain.ClientePedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "pedido-service/api/pedidos")
@Slf4j
@RequiredArgsConstructor
public class PedidoResource {
    private final PedidoService pedidoService;


    @GetMapping
    public String status(){
        log.info("Obtendo o status do microservice de pedidos");
        return "ok";
    }

    @GetMapping(value="/cpf")
    public ResponseEntity save (@RequestBody PedidoClienteRequest request){
        log.info("Obtendo os pedidos pelo cpf");
        ClientePedido clientePedido =  pedidoService.getCliente(request.getCpf());
        return ResponseEntity.ok(clientePedido);
    }

}
