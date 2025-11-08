package com.estudo.demo.DTOs.response;

import com.estudo.demo.enums.TypePerson;

public record PersonResponseDTO (

    Long id,
    String name,
    String cpf,
    String phone,
    TypePerson typePerson,
    boolean active
){}