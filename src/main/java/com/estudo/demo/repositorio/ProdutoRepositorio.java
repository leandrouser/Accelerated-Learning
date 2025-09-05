package com.estudo.demo.repositorio;

import com.estudo.demo.model.Produtos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProdutoRepositorio extends JpaRepository<Produtos, Long> {

    @Query("SELECT p FROM Produtos p WHERE " +
            "LOWER(p.nome) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(p.codigoBarras) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "CAST(p.id AS string) LIKE CONCAT('%', :term, '%') OR " +
            "LOWER(p.categorias.nome) LIKE LOWER(CONCAT('%', :term, '%'))")
    Page<Produtos> searchByMultipleFields(@Param("term") String term, Pageable pageable);


}

