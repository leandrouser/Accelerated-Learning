package com.estudo.demo.DTOs.requestDTO;

import com.estudo.demo.enums.TipoPessoa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PessoaRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    private String telefone;
    private String endereco;

    @NotNull(message = "Tipo de pessoa é obrigatório")
    private TipoPessoa tipo;

    private String senha;
    private Boolean ativo;
}