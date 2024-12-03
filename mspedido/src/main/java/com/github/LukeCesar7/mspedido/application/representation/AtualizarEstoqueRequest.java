package com.github.LukeCesar7.mspedido.application.representation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AtualizarEstoqueRequest {

    @NotNull
    @NotEmpty(message = "A lista de produtos não pode estar vazia.")
    @Valid
    private List<ProdutoRequest> produtoRequestList;

}
