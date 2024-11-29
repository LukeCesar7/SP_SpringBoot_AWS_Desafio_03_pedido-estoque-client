package com.github.LukeCesar7.mspedido.application.representation;

import com.github.LukeCesar7.mspedido.domain.ClientePedidoStatus;
import com.github.LukeCesar7.mspedido.domain.Produto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PedidoSaveRequest {
    private ClientePedidoStatus status;
    private List<ProductRequest> produtos = new ArrayList<>();
    private Integer quantidade;
}
