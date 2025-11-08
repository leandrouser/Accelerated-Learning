package com.estudo.demo.DTOs.response;

public record CustomerResponseDTO(

    Long id,
    String username,
    String cpf,
    String phone,
    boolean active
){}