package com.estudo.demo.DTOs;

import com.estudo.demo.enums.MetodoPagamento;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProcessarPagamentoDTO {

    private Long vendaId;
    private MetodoPagamento metodoPagamento;
    private String tipoCartao;
    private BigDecimal valorPago;
    private BigDecimal desconto;
}
