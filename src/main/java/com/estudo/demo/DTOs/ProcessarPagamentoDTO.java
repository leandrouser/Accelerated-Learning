package com.estudo.demo.DTOs;

import com.estudo.demo.enums.MetodoPagamento; // Importe o enum
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data // (Assumindo Lombok)
@NoArgsConstructor // (Assumindo Lombok)
@AllArgsConstructor // (Assumindo Lombok)
public class ProcessarPagamentoDTO {

    @NotNull(message = "ID da Venda é obrigatório")
    private Long vendaId;

    @NotNull(message = "Valor pago é obrigatório")
    @Positive(message = "Valor pago deve ser positivo")
    private BigDecimal valorPago;

    @NotNull(message = "Método de pagamento é obrigatório")
    private MetodoPagamento metodoPagamento;

    private String tipoCartao; // Opcional, relevante para cartão

    // CAMPO RENOMEADO/AJUSTADO:
    // Este é o DESCONTO EXTRA aplicado no momento do pagamento.
    // Pode ser nulo ou zero se não houver desconto adicional.
    private BigDecimal descontoAdicional;
}