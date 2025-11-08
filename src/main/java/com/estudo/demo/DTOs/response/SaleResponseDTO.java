package com.estudo.demo.DTOs.response;

import com.estudo.demo.enums.StatusPayment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record SaleResponseDTO(
        Long id,
        String customerName,
        int amount,
        LocalDate dateSale,
        BigDecimal discountTotal,
        BigDecimal subtotal,
        StatusPayment status,
        BigDecimal value,
        List<SaleItemResponseDTO> itens
) {}