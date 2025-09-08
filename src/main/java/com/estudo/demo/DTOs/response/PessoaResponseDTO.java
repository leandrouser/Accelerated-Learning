package com.estudo.demo.DTOs.response;

import com.estudo.demo.enums.TipoPessoa;
import lombok.Data;

@Data
public class PessoaResponseDTO {

    private String nome;
    private String endereco;
    private String cpf;
    private String telefone;
    private TipoPessoa tipo;
    private boolean ativo;
}
