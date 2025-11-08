package com.estudo.demo.DTOs;

import com.estudo.demo.DTOs.response.SaleItemResponseDTO;
import com.estudo.demo.enums.MethodPayment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PagamentoItensDTO(
        Long paymentId,
        Long saleId,
        String customerName,
        BigDecimal totalValue,
        MethodPayment methodPayment,
        LocalDate paymentDate,
        List<SaleItemResponseDTO> itens
) {
}
