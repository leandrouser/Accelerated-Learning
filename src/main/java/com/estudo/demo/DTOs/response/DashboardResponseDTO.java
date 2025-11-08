package com.estudo.demo.DTOs.response;

import java.util.List;

public record DashboardResponseDTO(
        List<ProductResponseDTO> lowStockProducts,
        List<ProductResponseDTO> zeroStockProducts,
        List<SaleResponseDTO> recentSales
) {
}
