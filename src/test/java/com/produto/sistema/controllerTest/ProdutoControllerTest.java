package com.produto.sistema.controllerTest;

import com.produto.sistema.controller.ProdutoController;
import com.produto.sistema.entities.Produto;
import com.produto.sistema.services.ProdutoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProdutoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private ProdutoController produtoController;

    private String sku = "ONE0001";
    private Produto produto;
    private AutoCloseable openMocks;
    private String json = """
            {
                "sku": "ONE0001",
                "nome": "Action Figure Luffy",
                "preco": 5.90
            }
            """;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(produtoController).build();
        produto = Produto.builder()
                .sku(sku)
                .nome("Action Figure Luffy")
                .preco(5.90)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void testEncontrarProduto_Success() throws Exception {
        // Arrange
        when(produtoService.encontrarProduto(produto.getSku())).thenReturn(Optional.of(produto));

        // Act & Assert
        mockMvc.perform(get("/produtos/{sku}", sku))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("ONE0001"));
        verify(produtoService, times(1)).encontrarProduto(sku);
    }

    @Test
    void testEncontrarProduto_NotFound() throws Exception {
        // Arrange
        when(produtoService.encontrarProduto(produto.getSku())).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/produtos/{sku}", sku))
                .andExpect(status().isNotFound());
        verify(produtoService, times(1)).encontrarProduto(sku);
    }

    @Test
    void testEncontrarTodos_Success() throws Exception {
        // Arrange
        when(produtoService.encontrarProdutos()).thenReturn(List.of(produto));

        // Act & Assert
        mockMvc.perform(get("/produtos/todos", sku))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("ONE0001"));
        verify(produtoService, times(1)).encontrarProdutos();
    }

    @Test
    void testEncontrarTodos_NotFound() throws Exception {
        // Arrange
        when(produtoService.encontrarProdutos()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/produtos/todos", sku))
                .andExpect(status().isNotFound());
        verify(produtoService, times(1)).encontrarProdutos();
    }

    @Test
    void testCadastrarProduto_Success() throws Exception {
        // Arrange
        when(produtoService.cadastrarProduto(any(Produto.class))).thenReturn(produto);

        // Act & Assert
        mockMvc.perform(post("/produtos/cadastrar")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().json(json));
        verify(produtoService, times(1)).cadastrarProduto(any(Produto.class));
    }

    @Test
    void testCadastrarProduto_BadRequest() throws Exception {
        // Arrange
        when(produtoService.cadastrarProduto(any(Produto.class))).thenThrow(new IllegalArgumentException("Dados inválidos"));

        // Act & Assert
        mockMvc.perform(post("/produtos/cadastrar")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
        verify(produtoService, times(1)).cadastrarProduto(any(Produto.class));
    }

    @Test
    void testAtualizarProduto_Success() throws Exception {
        // Arrange
        when(produtoService.encontrarProduto(sku)).thenReturn(Optional.of(produto));
        when(produtoService.alterarProduto(any(Produto.class))).thenReturn(produto);

        // Act & Assert
        mockMvc.perform(put("/produtos/alterar")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
        verify(produtoService, times(1)).alterarProduto(any(Produto.class));
    }

    @Test
    void testAtualizarProduto_BadRequest() throws Exception {
        // Arrange
        when(produtoService.encontrarProduto(sku)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/produtos/alterar")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
        verify(produtoService, times(0)).alterarProduto(any(Produto.class));
    }

    @Test
    void testDeletarProduto_Success() throws Exception {
        // Arrange
        when(produtoService.encontrarProduto(produto.getSku())).thenReturn(Optional.of(produto));

        // Act & Assert
        mockMvc.perform(delete("/produtos/{sku}", sku))
                .andExpect(status().isNoContent());
        verify(produtoService, times(1)).deletarProduto(sku);
    }

    @Test
    void testDeletarProduto_BadRequest() throws Exception {
        // Arrange
        when(produtoService.encontrarProduto(produto.getSku())).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/produtos/{sku}", sku))
                .andExpect(status().isBadRequest());
        verify(produtoService, times(0)).deletarProduto(sku);
    }

}
