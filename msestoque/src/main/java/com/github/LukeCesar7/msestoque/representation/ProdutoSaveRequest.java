package com.github.LukeCesar7.msestoque.representation;

import com.github.LukeCesar7.msestoque.domain.Produto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoSaveRequest {

    @NotBlank(message = "O Produto deve ser nomeado")
    private String nome;
    @NotBlank(message = "Insira a descrição do produto")
    private String descricao;
    @NotBlank(message = "A quantidade mínima em estoque é obrigatória")
    @Min(value= 0, message = "A quantidade mínima é zero")
    private Integer quantidade;

    public Produto toModel(){
        return new Produto(nome,descricao,quantidade);
    }


}
