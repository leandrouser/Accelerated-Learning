package com.estudo.demo.repositorio;

import com.estudo.demo.model.ItensVendas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItensVendasRepositorio extends JpaRepository<ItensVendas, Long> {
    List<ItensVendas> findByVendaId(Long vendaId);

}
