package com.github.LukeCesar7.msestoque.application;

import com.github.LukeCesar7.msestoque.domain.Produto;
import com.github.LukeCesar7.msestoque.representation.AtualizarEstoqueRequest;
import com.github.LukeCesar7.msestoque.representation.ProdutoRequest;
import com.github.LukeCesar7.msestoque.representation.ProdutoResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.function.Function;

@Service
@AllArgsConstructor
@Slf4j
public class EstoqueService {
    private final EstoqueRepository repository;
    private final EstoqueRepository estoqueRepository;
    private final ModelMapper modelMapper;


//_____________________________________________________________________________________________________________SalvaProd
    @Transactional
    public Produto save(Produto produto) {
        return repository.save(produto);
    }
//______________________________________________________________________________________________________________AchaProd
    //Acha o produto pelo ID
    public Produto findById(Long id) {
        return repository.findById(id).orElseThrow(IllegalArgumentException::new);
    }
//_______________________________________________________________________________________________________________AllProd
    //Mostra todos os produtos no estoque
    public List<Produto> findAll() {
        return repository.findAll();
    }
//_______________________________________________________________________________________________________DisponivelByQty
    //Checa se tem ou n
    public List<ProdutoResponse> checarDisponibilidade(List<Long> produtosId) {
        List<Produto> lista = estoqueRepository.findProdutoByIdIn(produtosId);
        List<ProdutoResponse> estoqueResponse = (List<ProdutoResponse>) lista.stream()
                .map((Function<? super Produto, ?>) produto -> this.modelMapper.map(produto, ProdutoResponse.class))
                .toList();
        return estoqueResponse;
    }
//_______________________________________________________________________________________________________(!)AtualizarQte
public void atualizarEstoque(AtualizarEstoqueRequest atualizarEstoqueRequest) {
    log.info("Atualizando estoque");

    List<ProdutoRequest> produtoRequests = atualizarEstoqueRequest.getProdutoRequestList();
    List<Long> produtosId = produtoRequests.stream().map(ProdutoRequest::getProdutoId).toList();
    List<Produto> produtos = this.estoqueRepository.findProdutoByIdIn(produtosId);

    for (Produto produto : produtos) {
        ProdutoRequest requestCorrespondente = produtoRequests.stream()
                .filter(request -> request.getProdutoId().equals(produto.getId()))
                .findFirst()
                .orElse(null);

        if (requestCorrespondente != null) {
            if (requestCorrespondente.getQuantidade() > produto.getQuantidade()) {
                String mensagem = "Não temos quantidade de produto em estoque suficiente para o produto ID " + produto.getId();
                log.error(mensagem); // Adiciona um log de erro
                throw new ResourceNotFoundException(mensagem);
            }
            produto.setQuantidade(produto.getQuantidade() - requestCorrespondente.getQuantidade());
        }
    }

    this.estoqueRepository.saveAll(produtos);
}

    public void atualizarQuantidadeProdutoEstoque(AtualizarEstoqueRequest atualizarEstoqueRequest) {
        //Acha o produto no banco de dados
        log.info("Inserindo produto no Estoque");
        List<ProdutoRequest> produtoRequests = atualizarEstoqueRequest.getProdutoRequestList();
        List<Long> produtosId = produtoRequests.stream().map(produtoRequest -> produtoRequest.getProdutoId()).toList();
        List<Produto> produtos = this.estoqueRepository.findProdutoByIdIn(produtosId);
        for(Produto produto : produtos){
            for(ProdutoRequest produtoRequest : produtoRequests){
                if(produtoRequest.getProdutoId().equals(produto.getId())){
                    if (produtoRequest.getQuantidade() > produto.getQuantidade()){
                        throw new ResourceNotFoundException("Não possuimos essa quantidade em estoque do produto informado");
                    }
                    produto.setQuantidade(produto.getQuantidade() + produtoRequest.getQuantidade());
                    log.info("Produto Atualizado com Sucesso" + produto.toString());
                }
            }

        }
        this.estoqueRepository.saveAll(produtos);
    }
    //Tratamento de erro personalizado para toda a classe
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
//_____________________________________________________________________________________________________________(!)Delete
    public void removerProduto(Long produtoId) {
        Produto produto = estoqueRepository.findById(produtoId).
                orElseThrow(()->
                        new ResourceNotFoundException("Produto de ID " + produtoId + " não encontrado/não inserido"));
        estoqueRepository.delete(produto);
    }
}
