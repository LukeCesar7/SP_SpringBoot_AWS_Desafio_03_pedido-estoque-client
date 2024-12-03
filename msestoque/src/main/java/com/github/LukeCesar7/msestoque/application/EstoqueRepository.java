package com.github.LukeCesar7.msestoque.application;

import com.github.LukeCesar7.msestoque.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstoqueRepository extends JpaRepository<Produto, Long> {
    List<Produto> findProdutoByIdIn(List<Long> produtoIdLista);
}
