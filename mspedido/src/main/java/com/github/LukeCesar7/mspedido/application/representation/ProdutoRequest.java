package com.github.LukeCesar7.mspedido.application.representation;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequest {
    private Long produtoId;
    @NotBlank(message = "O produto deve ter um nome")
    private String nome;
    @NotNull(message = "A quantidade mínima é zero (0)")
    @Min(value = 0, message = "O valor mínimo é zero (0)")
    @Max(value = 100, message = "O valor máximo é cem (100)")
    private int quantidade;
}
