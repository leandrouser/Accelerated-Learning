package com.estudo.demo.repositorio;

import com.estudo.demo.model.Produtos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepositorio extends JpaRepository<Produtos, Long> {

    /**
     * CORRIGIDO:
     * 1. Mudou de 'JOIN FETCH p.categorias' para 'JOIN p.categoria'. O JOIN é necessário para o WHERE.
     * 2. Adicionado @EntityGraph para carregar 'categoria' eficientemente com a paginação.
     */
    @EntityGraph(attributePaths = {"categoria"})
    @Query("SELECT p FROM Produtos p JOIN p.categoria c " + // <-- 'categoria' (singular) e sem FETCH
            "WHERE p.id = :idTermo OR " +
            "LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(p.codigoBarras) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(c.nome) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Produtos> findByIdOrTextSearch(@Param("idTermo") Long idTermo, @Param("termo") String termo, Pageable pageable);

    /**
     * CORRIGIDO:
     * 1. Mudou de 'JOIN FETCH p.categorias' para 'JOIN p.categoria'.
     * 2. Adicionado @EntityGraph.
     */
    @EntityGraph(attributePaths = {"categoria"})
    @Query("SELECT p FROM Produtos p JOIN p.categoria c " + // <-- 'categoria' (singular) e sem FETCH
            "WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(p.codigoBarras) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(c.nome) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Produtos> findByTextSearch(@Param("termo") String termo, Pageable pageable);

    /**
     * CORRIGIDO:
     * 1. Removido o 'JOIN FETCH' da query principal. Ele não é necessário para o WHERE.
     * 2. O @EntityGraph cuidará de trazer a categoria junto.
     * 3. Seu countQuery já estava correto (Parabéns!).
     */
    @EntityGraph(attributePaths = {"categoria"})
    @Query(value = "SELECT p FROM Produtos p WHERE p.estoque <= :estoque", // <-- JOIN removido daqui
            countQuery = "SELECT count(p) FROM Produtos p WHERE p.estoque <= :estoque") // <-- Perfeito
    Page<Produtos> findByEstoqueLessThanEqualComCategoria(@Param("estoque") int estoque, Pageable pageable);

}