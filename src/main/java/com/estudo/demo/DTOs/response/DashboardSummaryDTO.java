package com.estudo.demo.DTOs.response;

import java.util.List;

public record DashboardSummaryDTO(
        List<ProductResponseDTO> lowStockProducts,
        List<ProductResponseDTO> outOfStockProducts,
        List<SaleResponseDTO> recentSales
) {}
