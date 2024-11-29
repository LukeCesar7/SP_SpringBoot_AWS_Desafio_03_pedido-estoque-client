package com.github.LukeCesar7.mspedido.infra.repository;

import com.github.LukeCesar7.mspedido.domain.ClientePedido;

import java.util.List;

public interface ClientePedidoRepository {
    List<ClientePedido> findByCpf(String cpf);
}
