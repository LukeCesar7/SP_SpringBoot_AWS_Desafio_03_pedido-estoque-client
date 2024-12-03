package com.github.LukeCesar7.mspedido.infra.repository;

import com.github.LukeCesar7.mspedido.domain.ClientePedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientePedidoRepository extends JpaRepository<ClientePedido, Long> {
    List<ClientePedido> findByCpf(String cpf);
}
