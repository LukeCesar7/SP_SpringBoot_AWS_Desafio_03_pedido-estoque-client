package com.github.LukeCesar7.mspedido.infra.repository;

import com.github.LukeCesar7.mspedido.domain.ClientePedido;
import com.github.LukeCesar7.mspedido.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    }
