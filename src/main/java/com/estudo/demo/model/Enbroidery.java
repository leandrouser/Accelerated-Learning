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
public class Enbroidery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // Boa prática para @Data + @Entity
    private Long id;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;

    @Column(name = "data_entrega")
    private LocalDate dataEntrega;

    @Column(name = "valor", nullable = false)
    private BigDecimal value;

    @Column(name = "descricao")
    private String descricao;

    // --- CORREÇÃO APLICADA AQUI ---
    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading é geralmente melhor
    @JoinColumn(name = "cliente_id", nullable = false)
    private Customer customer; // <-- TIPO CORRIGIDO PARA Cliente

    @Lob
    @Column(name = "arquivo")
    private byte[] arquivo;

    @Column(name = "nome_arquivo")
    private String nomeArquivo;
}