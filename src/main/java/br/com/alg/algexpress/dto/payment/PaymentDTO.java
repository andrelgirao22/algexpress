package br.com.alg.algexpress.dto.payment;

import br.com.alg.algexpress.domain.payment.Payment;
import br.com.alg.algexpress.domain.valueObjects.Money;
import br.com.alg.algexpress.domain.valueObjects.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentDTO(
    Long id,
    
    @NotNull(message = "Order ID is required")
    Long orderId,
    
    @NotNull(message = "Payment method type is required")
    PaymentMethod.PaymentType paymentMethodType,
    
    Payment.PaymentStatus status,
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    BigDecimal amount,
    
    BigDecimal amountPaid,
    BigDecimal changeAmount,
    LocalDateTime paymentDateTime,
    String transactionId,
    String authorizationCode
) {
    
    public static PaymentDTO fromEntity(Payment payment) {
        return new PaymentDTO(
            payment.getId(),
            payment.getOrder() != null ? payment.getOrder().getId() : null,
            payment.getPaymentMethod() != null ? payment.getPaymentMethod().getType() : null,
            payment.getStatus(),
            payment.getAmount() != null ? payment.getAmount().getAmount() : null,
            payment.getAmountPaid() != null ? payment.getAmountPaid().getAmount() : null,
            payment.getChange() != null ? payment.getChange().getAmount() : null,
            payment.getPaymentDateTime(),
            payment.getTransactionInfo() != null ? payment.getTransactionInfo().getTransactionId() : null,
            payment.getTransactionInfo() != null ? payment.getTransactionInfo().getAuthorizationCode() : null
        );
    }
    
    public Payment toEntity() {
        Payment payment = new Payment();
        payment.setId(this.id);
        
        if (this.paymentMethodType != null) {
            payment.setPaymentMethod(new PaymentMethod(this.paymentMethodType));
        }
        
        payment.setStatus(this.status);
        
        if (this.amount != null) {
            payment.setAmount(Money.of(this.amount));
        }
        
        if (this.amountPaid != null) {
            payment.setAmountPaid(Money.of(this.amountPaid));
        }
        
        payment.setPaymentDateTime(this.paymentDateTime);
        
        return payment;
    }
}