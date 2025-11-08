package com.estudo.demo.DTOs.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmbroideryResponseDTO (
        Long id,
        LocalDate dateRegistration,
        LocalDate dateDelivery,
        BigDecimal value,
        String description,
        String fileName,
        String customername
){}
