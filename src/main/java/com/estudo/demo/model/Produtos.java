package com.estudo.demo.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "produtos")
public class Produtos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_barras", length = 13)
    private String codigoBarras;

    @Column(length = 50, nullable = false, unique = true)
    private String nome;

    @Column(nullable = true)
    private String descricao;

    @Min(value = 0, message = "Estoque não pode ficar negativo")
    private int estoque;

    @Column(precision = 5, scale = 2)
    private BigDecimal preco;

    @ManyToOne
    @JoinColumn(name = "id_categoria",  nullable = false)
    private Categorias categorias;



}
