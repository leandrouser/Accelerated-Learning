package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.PagamentoDetalhesDTO;
import com.estudo.demo.DTOs.PagamentoItensDTO;
import com.estudo.demo.DTOs.ProcessarPagamentoDTO;
import com.estudo.demo.enums.StatusPagamento;
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

        // --- Verificação de Status ---
        // (Recomendação: impedir pagamento duplicado)
        if (venda.getPagamento() == StatusPagamento.PAGO) { // Comparação direta
            throw new IllegalStateException("Esta venda já foi paga.");
        }

        BigDecimal descontoOriginalVenda = venda.getDescontoTotal() != null ? venda.getDescontoTotal() : BigDecimal.ZERO;
        BigDecimal descontoAdicionalPagamento = dto.getDescontoAdicional() != null ? dto.getDescontoAdicional() : BigDecimal.ZERO;

        // Validação (Opcional): Impedir desconto adicional negativo?
        if (descontoAdicionalPagamento.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Desconto adicional não pode ser negativo.");
        }

        // Desconto total efetivo = Desconto já na venda + Desconto extra do pagamento
        BigDecimal descontoTotalEfetivo = descontoOriginalVenda.add(descontoAdicionalPagamento);

        // --- Cálculos Finais ---
        BigDecimal subtotal = venda.getSubTotal(); // Usar o subtotal já calculado

        // Valor final a ser pago considera o desconto total efetivo
        BigDecimal valorTotalFinal = subtotal.subtract(descontoTotalEfetivo);

        // Garante que o valor final não seja negativo
        if (valorTotalFinal.compareTo(BigDecimal.ZERO) < 0) {
            valorTotalFinal = BigDecimal.ZERO;
        }

        // Validação do valor pago contra o valor final com TODOS os descontos
        if (dto.getValorPago().compareTo(valorTotalFinal) < 0) {
            throw new IllegalArgumentException("O valor pago (R$" + dto.getValorPago() +
                    ") é menor que o valor total a pagar com descontos (R$" + valorTotalFinal + ")");
        }

        BigDecimal troco = dto.getValorPago().subtract(valorTotalFinal);

        // --- Criação do Pagamento ---
        Pagamento pagamento = new Pagamento();
        pagamento.setVendaId(venda.getId());
        pagamento.setClienteId(venda.getCliente().getId());
        pagamento.setClienteNome(venda.getCliente().getUsername());
        pagamento.setSubTotal(subtotal);

        // O Pagamento registra o DESCONTO TOTAL EFETIVO aplicado
        pagamento.setDesconto(descontoTotalEfetivo);

        pagamento.setValorTotal(valorTotalFinal); // O valor que deveria ter sido pago
        pagamento.setValorPago(dto.getValorPago()); // O valor realmente pago
        pagamento.setTroco(troco);
        pagamento.setMetodoPagamento(dto.getMetodoPagamento());
        pagamento.setTipoCartao(dto.getTipoCartao());
        pagamento.setDataPagamento(LocalDate.now());

        pagamentoRepositorio.save(pagamento);

        // --- Atualização da Venda ---
        // A Venda reflete o estado FINAL após o pagamento
        venda.setPagamento(StatusPagamento.PAGO);
        venda.setDescontoTotal(descontoTotalEfetivo); // Atualiza com o desconto total efetivo
        venda.setValor(valorTotalFinal); // Atualiza com o valor final pago

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
