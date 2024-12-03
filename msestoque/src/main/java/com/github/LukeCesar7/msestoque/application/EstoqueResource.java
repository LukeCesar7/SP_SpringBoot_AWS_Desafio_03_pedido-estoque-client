package com.github.LukeCesar7.msestoque.application;

import com.github.LukeCesar7.msestoque.domain.Produto;
import com.github.LukeCesar7.msestoque.representation.AtualizarEstoqueRequest;
import com.github.LukeCesar7.msestoque.representation.ProdutoResponse;
import com.github.LukeCesar7.msestoque.representation.ProdutoSaveRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "estoque-service/api/estoque")
@RequiredArgsConstructor
@Slf4j
public class EstoqueResource {

    private final EstoqueService estoqueService;

    public String Status(){
        log.info("Status do Estoque de Serviço");
        return "okk";
    }
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> getAllEstoque() {
        List<Produto> estoque = estoqueService.findAll();
        List<ProdutoResponse> exibirEstoque = estoque.stream()
                .map(ProdutoResponse::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(exibirEstoque);
    }

    @PostMapping
    public ResponseEntity inserirProduto(@Valid @RequestBody ProdutoSaveRequest request) {
        Produto produto = request.toModel();
        estoqueService.save(produto);
        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("id={id}")
                .buildAndExpand(produto.getId())
                .toUri();
        return ResponseEntity.created(headerLocation).build();
    }

    @GetMapping(params = "id")
    public ResponseEntity encontrarProdutoPorId(@RequestParam("id") Long id) {
        Produto produto = estoqueService.findById(id);
        if (produto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(produto);
    }

    @GetMapping(params = "produtoIds")
    public ResponseEntity<List<ProdutoResponse>> checarDisponibilidade(@RequestParam List<Long> produtoIds) {
        List<ProdutoResponse> disponibilidadeEstoque = this.estoqueService.checarDisponibilidade(produtoIds);
        return ResponseEntity.ok().body(disponibilidadeEstoque);
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public void atualizarEstoque(@Valid @RequestBody AtualizarEstoqueRequest request) {
        this.estoqueService.atualizarEstoque(request);
    }

    @PatchMapping
    public void  alterarQuantidadeProduto(@Valid @RequestBody AtualizarEstoqueRequest request) {
        this.estoqueService.atualizarQuantidadeProdutoEstoque(request);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        estoqueService.removerProduto(id);
        return ResponseEntity.noContent().build();
    }
}