package com.estudo.demo.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoResponseDTO {

    private Long id;
    private String codigoBarras;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String categoriaNome;
    private int estoque;

}
