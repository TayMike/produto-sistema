package com.produto.sistema.controller;

import com.produto.sistema.services.ProdutoService;
import com.produto.sistema.entities.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    ProdutoService produtoService;

    @GetMapping("/{sku}")
    public ResponseEntity<Produto> encontrarProduto(@PathVariable String sku) {
        Optional<Produto> produto = produtoService.encontrarProduto(sku);
        if (produto.isPresent()) {
            return ResponseEntity.ok(produto.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Produto>> encontrarProdutos() {
        List<Produto> produtos = produtoService.encontrarProdutos();
        if (produtos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(produtos);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Produto> cadastrarProduto(@RequestBody Produto produto) {
        try {
            Produto produtoCadastrado = produtoService.cadastrarProduto(produto);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{sku}")
                    .buildAndExpand(produtoCadastrado.getSku())
                    .toUri();
            return ResponseEntity.created(uri).body(produtoCadastrado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/alterar")
    public ResponseEntity<Produto> alterarProduto(@RequestBody Produto produto) {
        Optional<Produto> produtoVerificado = produtoService.encontrarProduto(produto.getSku());
        if (produtoVerificado.isPresent()) {
            Produto produtoAlterado = produtoService.alterarProduto(produto);
            return ResponseEntity.ok(produtoAlterado);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<Produto> deletarProduto(@PathVariable String sku) {
        Optional<Produto> produto = produtoService.encontrarProduto(sku);
        if (produto.isPresent()) {
            produtoService.deletarProduto(sku);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
