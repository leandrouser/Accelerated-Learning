package com.estudo.demo.repositorio;

import com.estudo.demo.model.Categorias;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepositorio extends JpaRepository<Categorias, Long> {
}
