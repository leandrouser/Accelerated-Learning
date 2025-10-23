package com.estudo.demo.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {

    private Long id;
    private String username;
    private String cpf;
    private String telefone;
    private String endereco;
    private boolean ativo;
}