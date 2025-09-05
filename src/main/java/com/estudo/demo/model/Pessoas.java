package com.estudo.demo.model;

import com.estudo.demo.enums.TipoPessoa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Entity
@Table(name = "pessoas")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pessoas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String nome;

    private String endereco;

    @CPF
    @Column(unique = true, length = 11)
    private String cpf;

    @Column(length = 11)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private TipoPessoa tipo;

    @Column(nullable = true)
    private String senha;

    private boolean ativo = true;

}
