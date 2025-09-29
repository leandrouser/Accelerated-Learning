package com.estudo.demo.DTOs.response;

import lombok.Data;

@Data
public class LoginResponseDTO {

    private String token;
    private String nome;
    private String tipo;
    private String senha;
    private String cpf;
}
