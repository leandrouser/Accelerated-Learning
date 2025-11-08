package com.estudo.demo.DTOs.requestDTO;

import com.estudo.demo.enums.StatusPayment;

import java.math.BigDecimal;
import java.util.List;

public record SaleRequestDTO(
        Long customerId,
        List<SaleItemRequestDTO> itens,
        BigDecimal discountTotal,
        StatusPayment payment
) {}
