package com.estudo.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequestDTO {

    private String codigoBarras;
    private String nome;
    private String descricao;
    private int estoque;
    private BigDecimal preco;
    private Long categoriaId;

}
