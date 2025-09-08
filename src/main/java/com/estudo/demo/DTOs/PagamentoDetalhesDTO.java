package com.estudo.demo.DTOs;

import com.estudo.demo.model.ItensVendas;
import com.estudo.demo.model.Pagamento;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PagamentoDetalhesDTO {
    private Pagamento pagamento;
    private List<ItensVendas> itens;
}
