package com.estudo.demo.DTOs.requestDTO;

import com.estudo.demo.enums.TypePerson;

public record PersonRequestDTO (

   String name,
   String cpf,
   String phone,
   TypePerson typePerson,
   String password,
   Boolean active
){}