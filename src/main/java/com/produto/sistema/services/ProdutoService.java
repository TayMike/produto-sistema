package com.produto.sistema.services;

import com.produto.sistema.entities.Produto;
import com.produto.sistema.repositories.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ProdutoService {

    @Autowired
    ProdutoRepository produtoRepository;

    public Optional<Produto> encontrarProduto(String sku) {
        return produtoRepository.findById(sku);
    }

    public List<Produto> encontrarProdutos() {
        return produtoRepository.findAll();
    }

    public Produto cadastrarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto alterarProduto(Produto produtoNovo) {
        if(produtoRepository.existsById(produtoNovo.getSku())) {
            Produto produtoVelho = produtoRepository.getReferenceById(produtoNovo.getSku());
            produtoVelho.setNome(produtoNovo.getNome());
            produtoVelho.setPreco(produtoNovo.getPreco());
            return produtoRepository.save(produtoVelho);
        }
        return null;
    }

    public void deletarProduto(String sku) {
        produtoRepository.deleteById(sku);
    }

}
