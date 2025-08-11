package br.com.alg.algexpress.domain.payment;

import br.com.alg.algexpress.domain.order.Order;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;
    
    @Column(precision = 8, scale = 2, nullable = false)
    private BigDecimal amount;
    
    @Column(precision = 8, scale = 2)
    private BigDecimal change;
    
    @Column(name = "amount_paid", precision = 8, scale = 2)
    private BigDecimal amountPaid;
    
    @Column(name = "payment_date_time")
    private LocalDateTime paymentDateTime;
    
    @Column(name = "due_date_time")
    private LocalDateTime dueDateDateTime;
    
    @Column(name = "transaction_id", length = 100)
    private String transactionId;
    
    @Column(name = "authorization_code", length = 50)
    private String authorizationCode;
    
    @Column(length = 300)
    private String observations;
    
    @PrePersist
    private void prePersist() {
        if (status == null) {
            status = PaymentStatus.PENDING;
        }
    }
    
    public enum PaymentMethod {
        CASH("Cash"),
        CREDIT_CARD("Credit Card"),
        DEBIT_CARD("Debit Card"),
        PIX("PIX"),
        MEAL_VOUCHER("Meal Voucher"),
        FOOD_VOUCHER("Food Voucher");
        
        private final String description;
        
        PaymentMethod(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum PaymentStatus {
        PENDING,
        PROCESSING,
        APPROVED,
        REJECTED,
        CANCELLED,
        REFUNDED
    }
    
    public BigDecimal calculateChange() {
        if (paymentMethod == PaymentMethod.CASH && amountPaid != null) {
            BigDecimal calculatedChange = amountPaid.subtract(amount);
            return calculatedChange.compareTo(BigDecimal.ZERO) > 0 ? calculatedChange : BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }
    
    public boolean isApproved() {
        return status == PaymentStatus.APPROVED;
    }
    
    public boolean requiresChange() {
        return paymentMethod == PaymentMethod.CASH && 
               amountPaid != null && 
               amountPaid.compareTo(amount) > 0;
    }
    
    @PreUpdate
    @PostLoad
    private void updateChange() {
        this.change = calculateChange();
    }
}