package com.estudo.demo.DTOs.requestDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BordadoRequestDTO {

    // Data de Entrega pode ser opcional? Se sim, remova @NotNull
    @NotNull(message = "Data de entrega é obrigatória")
    private LocalDate dataEntrega;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo") // Adicione validação
    private BigDecimal valor;

    private String descricao;

    @NotNull(message = "ID do Cliente é obrigatório")
    private Long cliente_id;

    // Estes não devem vir no DTO de requisição, são processados pelo Controller/Service
    // private byte[] arquivo;
    // private String nomeArquivo;

}