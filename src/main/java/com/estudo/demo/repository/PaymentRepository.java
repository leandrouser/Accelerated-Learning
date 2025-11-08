package com.estudo.demo.repository;

import com.estudo.demo.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByCustomerId(Long customerId);
    Optional<Payment> findBySaleId(Long saleId);
    boolean existsBySaleId(Long saleId);
}