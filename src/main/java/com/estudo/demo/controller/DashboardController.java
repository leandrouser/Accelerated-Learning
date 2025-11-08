package com.estudo.demo.controller;

import com.estudo.demo.DTOs.response.DashboardSummaryDTO;
import com.estudo.demo.Servico.DashboardService;
import com.estudo.demo.repository.ProductRepository;
import com.estudo.demo.repository.SaleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;
    private final DashboardService dashboardService;



    public DashboardController(ProductRepository productRepository, SaleRepository saleRepository, DashboardService dashboardService) {
        this.productRepository = productRepository;
        this.saleRepository = saleRepository;
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary() {
        return ResponseEntity.ok(dashboardService.getDashboardSummary());
    }

}
