package com.estudo.demo.repository;

import com.estudo.demo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUsername(String username);
    Optional<Customer> findByCpf(String cpf);
    boolean existsByCpfAndIdNot(String cpf, Long id);
    boolean existsByUsernameAndIdNot(String username, Long id);

    // 🔍 Consulta genérica combinando ID, nome ou CPF
    @Query("SELECT c FROM Customer c WHERE " +
            "CAST(c.id AS string) LIKE %:term% OR " +
            "LOWER(c.username) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "c.cpf LIKE %:term%")
    List<Customer> searchCustomer(String term);
}