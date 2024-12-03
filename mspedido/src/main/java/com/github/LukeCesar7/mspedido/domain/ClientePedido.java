package com.github.LukeCesar7.mspedido.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class ClientePedido {

    @Id
    private Long id;
    @Column(unique = true,nullable = false)
    private String cpf;
    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos;
}
