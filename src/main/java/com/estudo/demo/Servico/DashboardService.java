package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.response.DashboardSummaryDTO;
import com.estudo.demo.DTOs.response.ProductResponseDTO;
import com.estudo.demo.DTOs.response.SaleResponseDTO;
import com.estudo.demo.model.Product;
import com.estudo.demo.model.Sales;
import com.estudo.demo.repository.ProductRepository;
import com.estudo.demo.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;

    public DashboardSummaryDTO getDashboardSummary() {
        List<ProductResponseDTO> lowStock = productRepository.findLowStockProducts()
                .stream()
                .map(this::toProductResponseDTO)
                .toList();

        List<ProductResponseDTO> outOfStock = productRepository.findOutOfStockProducts()
                .stream()
                .map(this::toProductResponseDTO)
                .toList();

        // Pega as 5 últimas vendas
        List<SaleResponseDTO> recentSales = saleRepository.findRecentSales(PageRequest.of(0, 5));
        return new DashboardSummaryDTO(lowStock, outOfStock, recentSales);
    }

    private ProductResponseDTO toProductResponseDTO(Product p) {
        return new ProductResponseDTO(
                p.getId(),
                p.getBarCode(),
                p.getProductName(),
                p.getDescription(),
                p.getPrice(),
                p.getPriceCost(),
                p.getTypeCategory(),
                p.getStock()
        );
    }

    private SaleResponseDTO toSaleResponseDTO(Sales s) {
        return new SaleResponseDTO(
                s.getId(),
                s.getCustomer().getUsername(),
                s.getAmount(),
                s.getDataSale(),
                s.getDiscountTotal(),
                s.getSubTotal(),
                s.getPayment(),
                s.getValue(),
                null
        );
    }
}
