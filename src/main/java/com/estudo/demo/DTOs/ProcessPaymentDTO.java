package com.estudo.demo.DTOs;

import com.estudo.demo.enums.MethodPayment;
import java.math.BigDecimal;

public record ProcessPaymentDTO(

        Long saleId,
        BigDecimal valuePaid,
        MethodPayment paymentMethod,
        String typeCard,
        BigDecimal additionalDiscount
        ){}