package com.estudo.demo.DTOs;

import com.estudo.demo.enums.TipoPessoa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PessoaRequestDTO {

    private Long id;
    private String nome;
    private String endereco;
    private String cpf;
    private String telefone;
    private TipoPessoa tipo;
    private String senha;
    private Boolean ativo;
}
