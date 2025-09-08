package com.estudo.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValorPagoDTO {
    private Long vendaId;
    private int totalItems;
    private BigDecimal subTotal;
    private BigDecimal desconto;
    private BigDecimal valorTotal;
}
