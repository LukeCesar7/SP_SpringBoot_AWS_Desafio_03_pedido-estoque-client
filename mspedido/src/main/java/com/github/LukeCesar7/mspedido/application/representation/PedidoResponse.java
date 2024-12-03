package com.github.LukeCesar7.mspedido.application.representation;

import com.github.LukeCesar7.mspedido.domain.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PedidoResponse {

    private String cpf;
    private String numeroPedido;

    private StatusPedido status;
    private List<ProdutoResponse> produtos;


    public static PedidoResponse fromModel(PedidoResponse model){
        return new PedidoResponse(
                model.getCpf(),
                model.getNumeroPedido(),
                model.getStatus(),
                model.getProdutos()
        );
    }
    }