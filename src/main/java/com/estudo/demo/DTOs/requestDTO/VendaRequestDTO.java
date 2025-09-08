package com.estudo.demo.DTOs.requestDTO;

import com.estudo.demo.DTOs.ItensVendasDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaRequestDTO {

    private Long clienteId;
    private BigDecimal descontoTotal;
    private List<ItensVendasDTO> itens;
    private int quantidade;
    private BigDecimal subTotal;
    private BigDecimal total;

}
