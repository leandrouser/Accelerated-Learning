package com.estudo.demo.repositorio;

import com.estudo.demo.model.Bordados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BordadoRepositorio extends JpaRepository<Bordados, Long> {
}
