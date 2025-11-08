package com.estudo.demo.repository;

import com.estudo.demo.model.People;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<People, Long>{
    Optional<People> findByName(String nome);

    Optional<People> findByCpf(String cpf);

    boolean existsByCpfAndIdNot(String cpf, Long id);


}