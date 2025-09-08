package com.estudo.demo.DTOs;

import com.estudo.demo.model.ItensVendas;
import com.estudo.demo.model.Pagamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoItensDTO {
    private Pagamento pagamento;
    private List<ItensVendas> itens;
}
