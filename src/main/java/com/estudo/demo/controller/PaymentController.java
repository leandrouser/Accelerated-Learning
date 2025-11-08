package com.estudo.demo.controller;

import com.estudo.demo.DTOs.PaymentDetalhesDTO;
import com.estudo.demo.DTOs.ProcessPaymentDTO;
import com.estudo.demo.DTOs.PagamentoItensDTO;
import com.estudo.demo.Servico.PaymentService;
import com.estudo.demo.model.Payment;

import com.estudo.demo.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
@Validated
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentService paymentService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    @PostMapping
    public ResponseEntity<Payment> confirmarPagamento(@Valid @RequestBody ProcessPaymentDTO dto) {
        Payment payment = paymentService.confirmarPagamento(dto);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/detalhes/{pagamentoId}")
    public ResponseEntity<PaymentDetalhesDTO> getDetalhesPagamento(@PathVariable Long pagamentoId) {
        PaymentDetalhesDTO detalhes = paymentService.getDetalhesPagamento(pagamentoId);
        return ResponseEntity.ok(detalhes);
    }

    @GetMapping("/itens/{pagamentoId}")
    public ResponseEntity<PagamentoItensDTO> getPagamentoComItens(@PathVariable Long pagamentoId) {
        PagamentoItensDTO pagamentoComItens = paymentService.getPagamentoComItens(pagamentoId);
        return ResponseEntity.ok(pagamentoComItens);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PaymentDetalhesDTO>> getPagamentosPorCliente(@PathVariable Long clienteId) {
        List<PaymentDetalhesDTO> pagamentos = paymentService.getPagamentosPorCliente(clienteId);
        return ResponseEntity.ok(pagamentos);
    }

    @GetMapping("/venda/{vendaId}")
    public ResponseEntity<PaymentDetalhesDTO> getPagamentoPorVenda(@PathVariable Long vendaId) {
        Payment payment = paymentRepository.findBySaleId(vendaId)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado para a venda: " + vendaId));

        return ResponseEntity.ok(PaymentDetalhesDTO.fromEntity(payment));
    }
}