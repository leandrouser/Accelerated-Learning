package com.estudo.demo.DTOs.response;

public record LoginResponseDTO (
    String token,
    String type,
    String name,
    String cpf
){}
