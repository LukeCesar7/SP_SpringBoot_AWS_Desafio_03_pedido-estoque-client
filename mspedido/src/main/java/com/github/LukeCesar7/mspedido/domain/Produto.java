package com.github.LukeCesar7.mspedido.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true,nullable = false)
    private String nome;
    @Column(unique = true,nullable = false)
    private String descricao;
    private Integer quantidadeItens;


}
