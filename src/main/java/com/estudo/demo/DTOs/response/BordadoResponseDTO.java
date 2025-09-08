package com.estudo.demo.DTOs.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BordadoResponseDTO {

    private long id;
    private LocalDate dataCadastro;
    private LocalDate dataEntrega;
    private BigDecimal valor;
    private String clienteNome;
    private String descricao;
    private String nomeArquivo;
}
