package com.estudo.demo.controller;

import com.estudo.demo.DTOs.requestDTO.ProductRequestDTO;
import com.estudo.demo.DTOs.response.ProductResponseDTO;
import com.estudo.demo.Servico.ProductService;
import com.estudo.demo.model.Product;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;


@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> criateProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO response = productService.create(productRequestDTO);
        return ResponseEntity
                .created(URI.create("/api/products/" + response.id()))
                .body(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Busca paginada de produtos", description = "Busca produtos por nome, código de barras ou categoria (enum).")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam(required = false) String termo,
            @PageableDefault(size = 10, sort = "productName", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Product> result = productService.searchProducts(termo, pageable);
        return ResponseEntity.ok(result);
    }
}
