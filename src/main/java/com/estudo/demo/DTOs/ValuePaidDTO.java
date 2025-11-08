package com.estudo.demo.DTOs;

import java.math.BigDecimal;

public record ValuePaidDTO(
    Long saleId,
    int totalItems,
    BigDecimal subTotal,
    BigDecimal discount,
    BigDecimal valueTotal
){}
