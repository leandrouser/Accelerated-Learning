package com.estudo.demo.DTOs;

import com.estudo.demo.enums.TipoPessoa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PessoaResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String endereco;
    private TipoPessoa tipo;
    private boolean ativo;
}
