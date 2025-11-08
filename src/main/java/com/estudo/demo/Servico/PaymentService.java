package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.PagamentoItensDTO;
import com.estudo.demo.DTOs.PaymentDetalhesDTO;
import com.estudo.demo.DTOs.ProcessPaymentDTO;
import com.estudo.demo.DTOs.response.SaleItemResponseDTO;
import com.estudo.demo.enums.MethodPayment;
import com.estudo.demo.enums.StatusPayment;
import com.estudo.demo.model.Payment;
import com.estudo.demo.model.Sales;
import com.estudo.demo.repository.PaymentRepository;
import com.estudo.demo.repository.SaleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PaymentService {

    private final SaleRepository saleRepository;
    private final PaymentRepository paymentRepository;

    public PaymentService(SaleRepository saleRepository, PaymentRepository paymentRepository) {
        this.saleRepository = saleRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Payment confirmarPagamento(ProcessPaymentDTO dto) {
        Sales venda = saleRepository.findById(dto.saleId())
                .orElseThrow(() -> new EntityNotFoundException("Venda não encontrada com o código: " + dto.saleId()));

        // Verificação de Status
        if (venda.getPayment() == StatusPayment.PAID) {
            throw new IllegalStateException("Esta venda já foi paga.");
        }

        BigDecimal descontoOriginalVenda = venda.getDiscountTotal() != null ? venda.getDiscountTotal() : BigDecimal.ZERO;
        BigDecimal descontoAdicionalPagamento = dto.additionalDiscount() != null ? dto.additionalDiscount() : BigDecimal.ZERO;

        if (descontoAdicionalPagamento.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Desconto adicional não pode ser negativo.");
        }

        BigDecimal descontoTotalEfetivo = descontoOriginalVenda.add(descontoAdicionalPagamento);
        BigDecimal subtotal = venda.getSubTotal();

        BigDecimal valorTotalFinal = subtotal.subtract(descontoTotalEfetivo);
        if (valorTotalFinal.compareTo(BigDecimal.ZERO) < 0) {
            valorTotalFinal = BigDecimal.ZERO;
        }

        if (dto.valuePaid().compareTo(valorTotalFinal) < 0) {
            throw new IllegalArgumentException("O valor pago (R$" + dto.valuePaid() +
                    ") é menor que o valor total a pagar com descontos (R$" + valorTotalFinal + ")");
        }

        BigDecimal troco = dto.valuePaid().subtract(valorTotalFinal);

        // Validação para tipo de cartão
        if ((dto.paymentMethod() == MethodPayment.CARTAO_CREDITO ||
                dto.paymentMethod() == MethodPayment.CARTAO_DEBITO) &&
                (dto.typeCard() == null || dto.typeCard().trim().isEmpty())) {
            throw new IllegalArgumentException("Tipo de cartão é obrigatório para pagamento com cartão.");
        }

        // Criação do Pagamento
        Payment payment = new Payment();
        payment.setSale(venda);
        payment.setCustomer(venda.getCustomer());
        payment.setSubTotal(subtotal);
        payment.setDiscount(descontoTotalEfetivo);
        payment.setTotalValue(valorTotalFinal);
        payment.setValuePaid(dto.valuePaid());
        payment.setChange(troco);
        payment.setMethodPayment(dto.paymentMethod());
        payment.setTypeCard(dto.typeCard());
        payment.setPaymentDate(LocalDate.now());

        Payment savedPayment = paymentRepository.save(payment);

        // Atualização da Venda
        venda.setPayment(StatusPayment.PAID);
        venda.setDiscountTotal(descontoTotalEfetivo);
        venda.setValue(valorTotalFinal);

        saleRepository.save(venda);

        return savedPayment;
    }

    public PaymentDetalhesDTO getDetalhesPagamento(Long pagamentoId) {
        Payment payment = paymentRepository.findById(pagamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado: " + pagamentoId));

        return PaymentDetalhesDTO.fromEntity(payment);
    }

    public PagamentoItensDTO getPagamentoComItens(Long pagamentoId) {
        Payment payment = paymentRepository.findById(pagamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado: " + pagamentoId));

        // Converter itens da venda para DTO
        List<SaleItemResponseDTO> itensDTO = payment.getSale().getItems().stream()
                .map(item -> new SaleItemResponseDTO(
                        item.getProduct().getId(),
                        item.getProduct().getProductName(),
                        item.getProduct().getPrice(),
                        item.getAmount()
                ))
                .toList();

        return new PagamentoItensDTO(
                payment.getId(),
                payment.getSale().getId(),
                payment.getCustomer().getUsername(),
                payment.getTotalValue(),
                payment.getMethodPayment(),
                payment.getPaymentDate(),
                itensDTO
        );
    }

    public List<PaymentDetalhesDTO> getPagamentosPorCliente(Long clienteId) {
        List<Payment> pagamentos = paymentRepository.findByCustomerId(clienteId);
        return pagamentos.stream()
                .map(PaymentDetalhesDTO::fromEntity)
                .toList();
    }
}