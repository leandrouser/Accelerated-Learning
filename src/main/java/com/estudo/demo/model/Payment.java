package com.estudo.demo.model;

import com.estudo.demo.enums.MethodPayment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pagamentos")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venda_id", nullable = false)
    private Sales sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Customer customer;

    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subTotal;

    @Column(name = "desconto", precision = 10, scale = 2)
    private BigDecimal discount;

    @Column(name = "valor_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalValue;

    @Column(name = "valor_pago", precision = 10, scale = 2, nullable = false)
    private BigDecimal valuePaid;

    @Column(name = "troco", precision = 10, scale = 2)
    private BigDecimal change;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pagamento", length = 20, nullable = false)
    private MethodPayment methodPayment;

    @Column(name = "tipo_cartao", length = 30)
    private String typeCard;

    @Column(name = "data_pagamento", nullable = false)
    private LocalDate paymentDate;
}