package br.com.alg.algexpress.dto.payment;

import br.com.alg.algexpress.domain.payment.Payment;
import br.com.alg.algexpress.domain.valueObjects.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponseDTO(
    Long id,
    Long orderId,
    PaymentMethod.PaymentType paymentMethodType,
    String paymentMethodDescription,
    Payment.PaymentStatus status,
    BigDecimal amount,
    BigDecimal amountPaid,
    BigDecimal changeAmount,
    LocalDateTime paymentDateTime,
    String transactionId,
    String authorizationCode
) {
    
    public static PaymentResponseDTO fromEntity(Payment payment) {
        return new PaymentResponseDTO(
            payment.getId(),
            payment.getOrder() != null ? payment.getOrder().getId() : null,
            payment.getPaymentMethod() != null ? payment.getPaymentMethod().getType() : null,
            payment.getPaymentMethod() != null ? payment.getPaymentMethod().getDescription() : null,
            payment.getStatus(),
            payment.getAmount() != null ? payment.getAmount().getAmount() : null,
            payment.getAmountPaid() != null ? payment.getAmountPaid().getAmount() : null,
            payment.getChange() != null ? payment.getChange().getAmount() : null,
            payment.getPaymentDateTime(),
            payment.getTransactionInfo() != null ? payment.getTransactionInfo().getTransactionId() : null,
            payment.getTransactionInfo() != null ? payment.getTransactionInfo().getAuthorizationCode() : null
        );
    }
}