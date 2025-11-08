package com.estudo.demo.DTOs.response;

import java.math.BigDecimal;

public record SaleItemResponseDTO(
        Long productId,
        String productName,
        BigDecimal productPrice,
        int quantity
) {}
