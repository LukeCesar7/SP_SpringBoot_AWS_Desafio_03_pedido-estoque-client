package com.github.LukeCesar7.mspedido.application;

import com.github.LukeCesar7.mspedido.application.representation.*;
import com.github.LukeCesar7.mspedido.domain.ClientePedido;
import com.github.LukeCesar7.mspedido.domain.Pedido;
import com.github.LukeCesar7.mspedido.domain.Produto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "pedido-service/api/pedidos")
@Slf4j
@RequiredArgsConstructor
public class PedidoResource {
    private final PedidoService pedidoService;

    @GetMapping
    public List<Pedido> listarTodosPedidos() {
        return pedidoService.findAll();
    }

    @GetMapping(params = "numeroPedido")
    public ResponseEntity pegarPedidoPeloNumero(@RequestParam("numeroPedido") String numeroPedido){
        Pedido pedido = pedidoService.pegarPedidoPeloNumero(numeroPedido);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping(params = "cpf")
    public ResponseEntity pegarPedidoPeloCpfCliente(@RequestParam("cpf") String cpf) {
        log.info("Obtendo os pedidos através do cpf");
        ClientePedido clientePedido = pedidoService.getCliente(cpf);
        return ResponseEntity.ok(clientePedido);
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> salvarPedido(@Validated @RequestBody PedidoSaveRequest request) throws Exception {
        log.info("Salvando pedido");
        PedidoResponse pedidoResponse = pedidoService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoResponse);

    }
}
