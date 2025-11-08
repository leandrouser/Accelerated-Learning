package com.estudo.demo.repository;

import com.estudo.demo.DTOs.response.ProductResponseDTO;
import com.estudo.demo.enums.Category;
import com.estudo.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT p FROM Product p
        WHERE (:termo IS NULL OR
              LOWER(p.productName) LIKE LOWER(CONCAT('%', :termo, '%'))
           OR LOWER(p.barCode) LIKE LOWER(CONCAT('%', :termo, '%'))
           OR LOWER(CAST(p.typeCategory AS string)) LIKE LOWER(CONCAT('%', :termo, '%')) )
    """)
    Page<Product> searchProducts(@Param("termo") String termo, Pageable pageable);

    Optional<Product> findByBarCode(String barCode);
    boolean existsByBarCode(String barCode);
    List<Product> findByTypeCategory(Category typeCategory);

    // Produtos com estoque baixo (<= 2)
    @Query("SELECT p FROM Product p WHERE p.stock <= 5 AND p.stock > 0")
    List<Product> findLowStockProducts();

    // Produtos com estoque zerado
    @Query("SELECT p FROM Product p WHERE p.stock = 0")
    List<Product> findOutOfStockProducts();
}
