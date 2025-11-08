package com.estudo.demo.repository;

import com.estudo.demo.DTOs.response.SaleResponseDTO;
import com.estudo.demo.model.Sales;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sales, Long> {

    List<Sales> findByCustomerId(Long customerId);
    List<Sales> findBySellerId(Long sellerId);
    List<Sales> findByDataSaleBetween(LocalDate startDate, LocalDate endDate);

    // 🟨 Últimas vendas (retorna as 5 mais recentes)
    @Query("""
           SELECT new com.estudo.demo.DTOs.response.SaleResponseDTO(
               s.id,
               s.customer.username,
               s.amount,
               s.dataSale,
               s.discountTotal,
               s.subTotal,
               s.payment,
               s.value,
               null
           )
           FROM Sales s
           ORDER BY s.dataSale DESC
           """)
    List<SaleResponseDTO> findRecentSales(Pageable pageable);

}
