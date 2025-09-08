package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.PagamentoDetalhesDTO;
import com.estudo.demo.DTOs.PagamentoItensDTO;
import com.estudo.demo.DTOs.ProcessarPagamentoDTO;
import com.estudo.demo.model.ItensVendas;
import com.estudo.demo.model.Pagamento;
import com.estudo.demo.model.Vendas;
import com.estudo.demo.repositorio.PagamentoRepositorio;
import com.estudo.demo.repositorio.ItensVendasRepositorio;
import com.estudo.demo.repositorio.VendaRepositorio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PagamentoServico {

    private final VendaRepositorio vendaRepositorio;
    private final PagamentoRepositorio pagamentoRepositorio;
    private final ItensVendasRepositorio itensVendasRepositorio;

    public PagamentoServico(VendaRepositorio vendaRepositorio,
                            PagamentoRepositorio pagamentoRepositorio,
                            ItensVendasRepositorio itensVendasRepositorio) {
        this.vendaRepositorio = vendaRepositorio;
        this.pagamentoRepositorio = pagamentoRepositorio;
        this.itensVendasRepositorio = itensVendasRepositorio;
    }

    @Transactional
    public Pagamento confirmarPagamento(ProcessarPagamentoDTO dto) {
        Vendas venda = vendaRepositorio.findById(dto.getVendaId())
                .orElseThrow(() -> new EntityNotFoundException("Venda não encontrada com o código: " + dto.getVendaId()));

        BigDecimal desconto = dto.getDesconto() != null ? dto.getDesconto() : venda.getDescontoTotal();
        BigDecimal subtotal = venda.getItens().stream()
                .map(item -> item.getTotal() != null ? item.getTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal valorTotal = subtotal.subtract(desconto);

        if (dto.getValorPago().compareTo(valorTotal) < 0) {
            throw new IllegalArgumentException("O valor pago não pode ser menor que o valor total da venda: " + valorTotal);
        }

        BigDecimal troco = dto.getValorPago().subtract(valorTotal);

        Pagamento pagamento = new Pagamento();
        pagamento.setVendaId(venda.getId());
        pagamento.setClienteId(venda.getCliente().getId());
        pagamento.setClienteNome(venda.getCliente().getNome());
        pagamento.setSubTotal(subtotal);
        pagamento.setDesconto(desconto);
        pagamento.setValorTotal(valorTotal);
        pagamento.setValorPago(dto.getValorPago());
        pagamento.setTroco(troco);
        pagamento.setMetodoPagamento(dto.getMetodoPagamento());
        pagamento.setTipoCartao(dto.getTipoCartao());
        pagamento.setDataPagamento(LocalDate.now());

        pagamentoRepositorio.save(pagamento);

        venda.setPagamento("PAGA");
        vendaRepositorio.save(venda);

        return pagamento;
    }

    public PagamentoItensDTO getPagamentoComItens(Long pagamentoId) {
        Pagamento pagamento = pagamentoRepositorio.findById(pagamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado"));

        List<ItensVendas> itens = itensVendasRepositorio.findByVendaId(pagamento.getVendaId());

        return new PagamentoItensDTO(pagamento, itens);
    }

    public PagamentoDetalhesDTO getDetalhesPagamento(Long pagamentoId) {
        Pagamento pagamento = pagamentoRepositorio.findById(pagamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado"));

        List<ItensVendas> itens = itensVendasRepositorio.findByVendaId(pagamento.getVendaId());

        return new PagamentoDetalhesDTO(pagamento, itens);
    }
}
