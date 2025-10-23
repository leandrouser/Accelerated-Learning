package com.estudo.demo.repositorio;

import com.estudo.demo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByUsername(String username);
    Optional<Cliente> findByCpf(String cpf);
    boolean existsByCpfAndIdNot(String cpf, Long id);
    boolean existsByUsernameAndIdNot(String username, Long id);
    // 🔍 Buscas dinâmicas para autocomplete em tempo real
    List<Cliente> findByUsernameContainingIgnoreCase(String username);
    List<Cliente> findByCpfContaining(String cpf);

    // 🔍 Consulta genérica combinando ID, nome ou CPF
    @Query("SELECT c FROM Cliente c WHERE " +
            "CAST(c.id AS string) LIKE %:term% OR " +
            "LOWER(c.username) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "c.cpf LIKE %:term%")
    List<Cliente> searchClientes(String term);
}