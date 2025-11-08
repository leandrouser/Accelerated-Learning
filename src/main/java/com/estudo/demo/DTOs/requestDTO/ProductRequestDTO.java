package com.estudo.demo.DTOs.requestDTO;

import com.estudo.demo.enums.Category;

import java.math.BigDecimal;

public record ProductRequestDTO (

    String barcode,
    String productName,
    String description,
    int stock,
    BigDecimal price,
    BigDecimal priceCost,
    Category typeCategory
){}

