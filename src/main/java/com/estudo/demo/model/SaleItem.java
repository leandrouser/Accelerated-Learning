package com.estudo.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "itens_venda")
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_venda", nullable = false)
    private Sales sale;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_produto", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int amount;
}
