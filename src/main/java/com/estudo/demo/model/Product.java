package com.estudo.demo.model;

import com.estudo.demo.enums.Category;
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
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_barras", length = 13)
    private String barCode;

    @Column(length = 50, nullable = false, unique = true)
    private String productName;

    @Column(nullable = true)
    private String description;

    @Min(value = 0, message = "Estoque não pode ficar negativo")
    private int stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,  length = 20)
    private Category typeCategory;

    @Column(precision = 5, scale = 2)
    private BigDecimal priceCost;

    @Column(precision = 5, scale = 2)
    private BigDecimal price;
}
