package com.produto.sistema.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "produto")
public class Produto {

    @Id
    private String sku;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private double preco;

}
