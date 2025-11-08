package com.estudo.demo.DTOs;

import com.estudo.demo.enums.MethodPayment;
import com.estudo.demo.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentDetalhesDTO(
        Long paymentId,
        Long saleId,
        String customerName,
        BigDecimal subTotal,
        BigDecimal discount,
        BigDecimal totalValue,
        BigDecimal valuePaid,
        BigDecimal change,
        MethodPayment methodPayment,
        String typeCard,
        LocalDate paymentDate
) {
    public static PaymentDetalhesDTO fromEntity(Payment payment) {
        return new PaymentDetalhesDTO(
                payment.getId(),
                payment.getSale().getId(),
                payment.getCustomer().getUsername(),
                payment.getSubTotal(),
                payment.getDiscount(),
                payment.getTotalValue(),
                payment.getValuePaid(),
                payment.getChange(),
                payment.getMethodPayment(),
                payment.getTypeCard(),
                payment.getPaymentDate()
        );
    }
}