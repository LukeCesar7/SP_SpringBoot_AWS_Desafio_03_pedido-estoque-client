package com.github.LukeCesar7.msestoque.representation;

import com.github.LukeCesar7.msestoque.domain.Produto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponse {

    private Long id;
    private String nome;
    private Integer quantidade;

    public Produto toModel() {
        return new Produto(id, nome, quantidade);
    }

    public static ProdutoResponse fromModel(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getQuantidade());
    }
}

