package com.estudo.demo.repositorio;

import com.estudo.demo.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PagamentoRepositorio extends JpaRepository<Pagamento, Long> {

    List<Pagamento> findByClienteId(Long clienteId);

    Optional<Pagamento> findByVendaId(Long vendaId);
}
