package com.github.LukeCesar7.mspedido.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    @Enumerated(EnumType.STRING)
    private ClientePedidoStatus status;
    @OneToMany
    private List<Produto> itens = new ArrayList<Produto>();
    private Integer quantidade;

    public Pedido(List<Produto> itens, ClientePedidoStatus status, Integer quantidade) {
        this.itens = itens;
        this.status = status;
        this.quantidade = quantidade;
    }


}


