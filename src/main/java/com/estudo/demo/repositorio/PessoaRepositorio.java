package com.estudo.demo.repositorio;

import com.estudo.demo.model.Pessoas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PessoaRepositorio extends JpaRepository<Pessoas, Long>{
    Optional<Pessoas> findByNome(String nome);

    Optional<Pessoas> findByCpf(String cpf);


}
