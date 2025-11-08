package com.estudo.demo.model;

import com.estudo.demo.enums.StatusPayment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vendedor_id", nullable = false)
    private People seller;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Customer customer;

    @Column(name = "data_venda", nullable = false)
    private LocalDate dataSale;

    @Column(name = "desconto_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal discountTotal;

    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subTotal;

    @Column(name = "valor_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private StatusPayment payment;

    @Column(nullable = false)
    private int amount;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> items = new ArrayList<>();
}
