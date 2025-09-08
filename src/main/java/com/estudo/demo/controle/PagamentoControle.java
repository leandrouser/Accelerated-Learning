package com.estudo.demo.controle;

import com.estudo.demo.DTOs.PagamentoDetalhesDTO;
import com.estudo.demo.DTOs.PagamentoItensDTO;
import com.estudo.demo.DTOs.ProcessarPagamentoDTO;
import com.estudo.demo.model.Pagamento;
import com.estudo.demo.Servico.PagamentoServico;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoControle {

    private final PagamentoServico pagamentoServico;

    public PagamentoControle(PagamentoServico pagamentoServico) {
        this.pagamentoServico = pagamentoServico;
    }

    @PostMapping
    public ResponseEntity<Pagamento> confirmarPagamento(@RequestBody ProcessarPagamentoDTO dto) {
        Pagamento pagamento = pagamentoServico.confirmarPagamento(dto);
        return ResponseEntity.ok(pagamento);
    }

    @GetMapping("/detalhes/{pagamentoId}")
    public ResponseEntity<PagamentoDetalhesDTO> getDetalhesPagamento(@PathVariable Long pagamentoId) {
        PagamentoDetalhesDTO detalhes = pagamentoServico.getDetalhesPagamento(pagamentoId);
        return ResponseEntity.ok(detalhes);
    }

    @GetMapping("/itens/{pagamentoId}")
    public ResponseEntity<PagamentoItensDTO> getPagamentoComItens(@PathVariable Long pagamentoId) {
        return ResponseEntity.ok(pagamentoServico.getPagamentoComItens(pagamentoId));
    }
}
