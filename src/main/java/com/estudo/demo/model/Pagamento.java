package com.estudo.demo.model;

import com.estudo.demo.enums.MetodoPagamento;
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
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long vendaId;
    private Long clienteId;
    private String clienteNome;

    private BigDecimal subTotal;
    private BigDecimal desconto;
    private BigDecimal valorTotal;
    private BigDecimal valorPago;
    private BigDecimal troco;

    @Enumerated(EnumType.STRING)
    @Column(length = 14)
    private MetodoPagamento metodoPagamento;

    private String tipoCartao;

    private LocalDate dataPagamento;
}
