package com.produto.sistema.servicesTest;

import com.produto.sistema.entities.Produto;
import com.produto.sistema.repositories.ProdutoRepository;
import com.produto.sistema.services.ProdutoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private AutoCloseable openMocks;
    private Produto produto;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);

        produto = Produto.builder()
                .sku("ONE0001")
                .nome("Action Figure Luffy")
                .preco(5.90)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void testEncontrarProduto() {
        // Arrange
        when(produtoRepository.findById(produto.getSku())).thenReturn(Optional.of(produto));

        // Act
        Optional<Produto> resultado = produtoService.encontrarProduto(produto.getSku());

        // Assert
        assertNotNull(resultado);
        assertEquals(produto.getSku(), resultado.get().getSku());
        verify(produtoRepository, times(1)).findById(produto.getSku());
    }

    @Test
    void testEncontrarTodosProduto() {
        // Arrange
        when(produtoRepository.findAll()).thenReturn(List.of(produto));

        // Act
        List<Produto> resultado = produtoService.encontrarProdutos();

        // Assert
        assertNotNull(resultado);
        assertEquals(produto.getSku(), resultado.getFirst().getSku());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void testCadastrarProduto() {
        // Arrange
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        // Act
        Produto resultado = produtoService.cadastrarProduto(produto);

        // Assert
        assertNotNull(resultado);
        assertEquals(produto.getSku(), resultado.getSku());
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void testAtualizarProduto_Success() {
        // Arrange
        when(produtoRepository.existsById(produto.getSku())).thenReturn(true);
        when(produtoRepository.getReferenceById(produto.getSku())).thenReturn(produto);
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        // Act
        Produto resultado = produtoService.alterarProduto(produto);

        // Assert
        assertNotNull(resultado);
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void testAtualizarProduto_Null() {
        // Arrange
        when(produtoRepository.existsById(produto.getSku())).thenReturn(false);

        // Act
        Produto resultado = produtoService.alterarProduto(produto);

        // Assert
        assertNull(resultado);
        verify(produtoRepository, times(0)).save(produto);
    }

    @Test
    void testDeletarProduto() {
        // Arrange & Act
        produtoService.deletarProduto(produto.getSku());

        // Assert
        verify(produtoRepository, times(1)).deleteById(produto.getSku());
    }

}
