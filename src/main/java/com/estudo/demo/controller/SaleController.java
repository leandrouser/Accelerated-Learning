package com.estudo.demo.controller;

import com.estudo.demo.DTOs.requestDTO.SaleRequestDTO;
import com.estudo.demo.DTOs.response.SaleItemResponseDTO;
import com.estudo.demo.DTOs.response.SaleResponseDTO;
import com.estudo.demo.Servico.SaleService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@Validated
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<SaleResponseDTO> createSale(@Valid @RequestBody SaleRequestDTO dto) {
        SaleResponseDTO response = saleService.createSale(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> searchSale(@PathVariable Long id) {
        SaleResponseDTO saleDTO = saleService.findById(id);
        return ResponseEntity.ok(saleDTO);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<SaleResponseDTO>> buscarVendasPorCliente(@PathVariable Long clienteId) {
        List<SaleResponseDTO> sales = saleService.findByCustomer(clienteId);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<List<SaleResponseDTO>> buscarVendasPorVendedor(@PathVariable Long vendedorId) {
        List<SaleResponseDTO> sales = saleService.findBySeller(vendedorId);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/{vendaId}/itens")
    public ResponseEntity<List<SaleItemResponseDTO>> listarItensVenda(@PathVariable Long vendaId) {
        List<SaleItemResponseDTO> itens = saleService.listarItensPorVenda(vendaId);
        return ResponseEntity.ok(itens);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<SaleResponseDTO>> buscarVendasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        // Implementar método no service para buscar por período
        // List<SaleResponseDTO> sales = saleService.findByPeriod(inicio, fim);
        // return ResponseEntity.ok(sales);

        return ResponseEntity.ok(Collections.emptyList());
    }
}