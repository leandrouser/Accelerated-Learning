package com.estudo.demo.DTOs.response;

import com.estudo.demo.enums.Category;

import java.math.BigDecimal;

public record ProductResponseDTO (

    Long id,
    String barcode,
    String name,
    String discription,
    BigDecimal price,
    BigDecimal priceCost,
    Category typeCategory,
    Integer stock
){

}
