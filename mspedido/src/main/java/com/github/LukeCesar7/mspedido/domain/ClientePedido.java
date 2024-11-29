package com.github.LukeCesar7.mspedido.domain;

import jakarta.persistence.*;

public class ClientePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cpf;
    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;
}
