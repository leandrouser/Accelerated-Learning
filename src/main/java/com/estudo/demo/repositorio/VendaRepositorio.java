package com.estudo.demo.repositorio;

import com.estudo.demo.model.Vendas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendaRepositorio extends JpaRepository<Vendas, Long> {
}
