package com.estudo.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Itens_venda")
public class ItensVendas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produtos produto;

    @ManyToOne
    @JoinColumn(name = "id_venda", nullable = false)
    private Vendas venda;

    @Column(nullable = false, length = 3)
    private Integer quantidade;

    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal desconto;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal total;
}
