package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.requestDTO.ProductRequestDTO;
import com.estudo.demo.DTOs.response.ProductResponseDTO;
import com.estudo.demo.model.Product;
import com.estudo.demo.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponseDTO create(ProductRequestDTO productRequestDTO) {
        Product product = new Product();
        product.setProductName(productRequestDTO.productName());
        product.setBarCode(productRequestDTO.barcode());
        product.setDescription(productRequestDTO.description());
        product.setStock(productRequestDTO.stock());
        product.setPrice(productRequestDTO.price());
        product.setPriceCost(productRequestDTO.priceCost());
        product.setTypeCategory(productRequestDTO.typeCategory());

        Product savedProduct = productRepository.save(product);

        return new ProductResponseDTO(
                savedProduct.getId(),
                savedProduct.getBarCode(),
                savedProduct.getProductName(),
                savedProduct.getDescription(),
                savedProduct.getPrice(),
                savedProduct.getPriceCost(),
                savedProduct.getTypeCategory(),
                savedProduct.getStock()
        );
    }

    public Page<Product> searchProducts(String termo, Pageable pageable) {
        try {
            return productRepository.searchProducts(termo, pageable);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar produtos: " + e.getMessage(), e);
        }
    }
}


