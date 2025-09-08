package com.estudo.demo.DTOs.response;

import com.estudo.demo.DTOs.ClienteDTO;
import com.estudo.demo.DTOs.ItensVendasDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaResponseDTO {
    private Long id;
    private ClienteDTO cliente;
    private int quantidade;
    private LocalDate dataVenda;
    private BigDecimal descontoTotal;
    private BigDecimal subtotal;
    private String pagamento;
    private List<ItensVendasDTO> itens;
    public BigDecimal getValor() {
        return subtotal.subtract(descontoTotal != null ? descontoTotal : BigDecimal.ZERO);
    }
}
