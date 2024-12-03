package com.github.LukeCesar7.msestoque.representation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProdutoRequest {


    @NotNull(message = "O ID do produto é necessário")
    private Long produtoId;

    @NotNull(message = "A quantidade do produto é obrigatória")
    @Min(value = 0, message = "O valor mínimo é zero (0)")
    @Max(value = 100, message = "O valor máximo é cem (100)")
    private Integer quantidade;
}
