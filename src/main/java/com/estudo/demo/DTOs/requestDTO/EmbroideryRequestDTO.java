package com.estudo.demo.DTOs.requestDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmbroideryRequestDTO (

    LocalDate dateDelivery,
    BigDecimal value,
    String discription,
    Long customer_id
){}