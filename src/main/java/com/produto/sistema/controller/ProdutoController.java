package com.produto.sistema.controller;

import com.produto.sistema.services.ProdutoService;
import com.produto.sistema.entities.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return produto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
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
            return ResponseEntity.ok(produtoCadastrado);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/alterar")
    public ResponseEntity<Produto> alterarProduto(@RequestBody Produto produto) {
        try {
            Produto produtoAlterado = produtoService.alterarProduto(produto);
            return ResponseEntity.ok(produtoAlterado);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/deletar/{cpf}")
    public ResponseEntity<Produto> deletarProduto(@PathVariable String cpf) {
        try {
            produtoService.deletarProduto(cpf);
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
