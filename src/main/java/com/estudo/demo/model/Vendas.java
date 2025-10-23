package com.estudo.demo.model;

import com.estudo.demo.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

@Data
@Table(name = "vendas")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Vendas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Pessoas vendedor;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "data_venda", nullable = false)
    private LocalDate dataVenda;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItensVendas> itens = new ArrayList<>();

    @Column(name = "desconto_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal descontoTotal;

    @Column(name = "subTotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subTotal;

    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING) // Salva o nome "PENDENTE", "PAGA" no BD
    @Column(nullable = false, length = 10)
    private StatusPagamento pagamento;

    @Column(nullable = false)
    private int quantidade;

}
