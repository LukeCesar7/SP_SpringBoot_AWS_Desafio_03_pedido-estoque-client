package com.github.LukeCesar7.mspedido.application.representation;

import com.github.LukeCesar7.mspedido.domain.Pedido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PedidoSaveRequest {

    @NotBlank(message = "O CPF não pode estar vazio")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 dígitos numéricos")
    private String cpf;
    private List<ProdutoRequest> produtosRequest;


}
