package com.estudo.demo.DTOs;


import com.estudo.demo.enums.TypePerson;

public record PersonResponseDTO(

     Long id,
     String name,
     String cpf,
     String phone,
     TypePerson typePerson,
     boolean active
     ){}
