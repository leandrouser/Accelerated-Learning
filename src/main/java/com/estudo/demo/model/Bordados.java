package com.estudo.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bordados")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Bordados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;

    @Column(name = "data_entrega")
    private LocalDate dataEntrega;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "descricao")
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Pessoas cliente;

    @Lob
    @Column(name = "arquivo")
    private byte[] arquivo;

    @Column(name = "nome_arquivo")
    private String nomeArquivo;
}
