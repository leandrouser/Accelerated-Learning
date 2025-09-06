package com.estudo.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItensVendasDTO {

    private Long produtoId;
    private String produtoNome;
    private String codigoBarras;
    private String categoriaNome;
    private Integer quantidade;
    private BigDecimal valor;
    private BigDecimal desconto;
    private BigDecimal total;
}
