package com.estudo.demo.repositorio;

import com.estudo.demo.model.Produtos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepositorio extends JpaRepository<Produtos, Long> {

    @Query("SELECT p FROM Produtos p JOIN FETCH p.categorias c " +
            "WHERE p.id = :idTermo OR " +
            "LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(p.codigoBarras) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(c.nome) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Produtos> findByIdOrTextSearch(@Param("idTermo") Long idTermo, @Param("termo") String termo, Pageable pageable);

    @Query("SELECT p FROM Produtos p JOIN FETCH p.categorias c " +
            "WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(p.codigoBarras) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(c.nome) LIKE LOWER(CONCAT('%', :termo, '%'))")

    Page<Produtos> findByTextSearch(@Param("termo") String termo, Pageable pageable);

    @Query("SELECT p FROM Produtos p JOIN FETCH p.categorias WHERE p.estoque = :estoque")
    Page<Produtos> findByEstoque(int estoque, Pageable pageable);
}

