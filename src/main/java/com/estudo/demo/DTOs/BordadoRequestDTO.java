package com.estudo.demo.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BordadoRequestDTO {

    @NotNull
    private LocalDate dataEntrega;

    @NotNull
    private BigDecimal valor;

    private String descricao;

    @NotNull
    private Long cliente_id;

    private byte[] arquivo;
    private String nomeArquivo;

}
