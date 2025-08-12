package br.com.alg.algexpress.dto.payment;

import br.com.alg.algexpress.domain.valueObjects.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequestDTO(
    @NotNull(message = "Order ID is required")
    Long orderId,
    
    @NotNull(message = "Payment method type is required")
    PaymentMethod.PaymentType paymentMethodType,
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    BigDecimal amount,
    
    BigDecimal amountPaid,
    String transactionId,
    String authorizationCode
) {}