package br.com.alg.algexpress.domain.payment;

import br.com.alg.algexpress.domain.order.Order;
import br.com.alg.algexpress.domain.valueObjects.Money;
import br.com.alg.algexpress.domain.valueObjects.PaymentMethod;
import br.com.alg.algexpress.domain.valueObjects.TransactionInfo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    
    @Embedded
    private PaymentMethod paymentMethod;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;
    
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "amount"))
    private Money amount;
    
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "change_amount"))
    private Money change;
    
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "amount_paid"))
    private Money amountPaid;
    
    @Column(name = "payment_date_time")
    private LocalDateTime paymentDateTime;
    
    @Column(name = "due_date_time")
    private LocalDateTime dueDateDateTime;
    
    @Embedded
    private TransactionInfo transactionInfo;
    
    @Column(length = 300)
    private String observations;
    
    @PrePersist
    private void prePersist() {
        if (status == null) {
            status = PaymentStatus.PENDING;
        }
    }
    
    // PaymentMethod agora é um Value Object em domain.valueObjects
    
    public enum PaymentStatus {
        PENDING,
        PROCESSING,
        APPROVED,
        REJECTED,
        CANCELLED,
        REFUNDED
    }
    
    public Money calculateChange() {
        if (paymentMethod != null && paymentMethod.requiresChange() && 
            amountPaid != null && amount != null) {
            Money calculatedChange = amountPaid.subtract(amount);
            return calculatedChange.isPositive() ? calculatedChange : Money.zero();
        }
        return Money.zero();
    }
    
    public boolean isApproved() {
        return status == PaymentStatus.APPROVED;
    }
    
    public boolean requiresChange() {
        return paymentMethod != null && paymentMethod.requiresChange() && 
               amountPaid != null && amount != null &&
               amountPaid.isGreaterThan(amount);
    }
    
    public boolean requiresAuthorization() {
        return paymentMethod != null && paymentMethod.requiresAuthorization();
    }
    
    public boolean hasCompleteTransactionInfo() {
        return transactionInfo != null && transactionInfo.isComplete();
    }
    
    public boolean canBeProcessed() {
        if (amount == null || amount.isZero() || amount.isNegative()) {
            return false;
        }
        if (requiresAuthorization() && (transactionInfo == null || transactionInfo.isEmpty())) {
            return false;
        }
        return status == PaymentStatus.PENDING;
    }
    
    @PreUpdate
    @PostLoad
    private void updateChange() {
        this.change = calculateChange();
    }
}