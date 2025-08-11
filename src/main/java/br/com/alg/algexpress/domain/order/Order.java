package br.com.alg.algexpress.domain.order;

import br.com.alg.algexpress.domain.address.Address;
import br.com.alg.algexpress.domain.customer.Customer;
import br.com.alg.algexpress.domain.payment.Payment;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payments;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType type;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id")
    private Address deliveryAddress;
    
    @Column(name = "order_date_time", nullable = false)
    private LocalDateTime orderDateTime;
    
    @Column(name = "estimated_date_time")
    private LocalDateTime estimatedDateTime;
    
    @Column(name = "completion_date_time")
    private LocalDateTime completionDateTime;
    
    @Column(precision = 8, scale = 2, nullable = false)
    private BigDecimal subtotal;
    
    @Column(name = "delivery_fee", precision = 8, scale = 2)
    private BigDecimal deliveryFee;
    
    @Column(precision = 8, scale = 2)
    private BigDecimal discount;
    
    @Column(precision = 8, scale = 2, nullable = false)
    private BigDecimal total;
    
    @Column(length = 300)
    private String observations;
    
    @Column(name = "estimated_time_minutes")
    private Integer estimatedTimeMinutes;
    
    @PrePersist
    private void prePersist() {
        if (orderDateTime == null) {
            orderDateTime = LocalDateTime.now();
        }
        if (status == null) {
            status = OrderStatus.PENDING;
        }
        calculateTotal();
    }
    
    @PreUpdate
    private void preUpdate() {
        calculateTotal();
    }
    
    public enum OrderStatus {
        PENDING,
        CONFIRMED,
        PREPARING,
        READY,
        OUT_FOR_DELIVERY,
        DELIVERED,
        CANCELLED
    }
    
    public enum OrderType {
        DELIVERY,
        PICKUP,
        DINE_IN
    }
    
    public BigDecimal calculateTotal() {
        BigDecimal calculatedSubtotal = items != null ? items.stream()
            .map(OrderItem::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;
            
        this.subtotal = calculatedSubtotal;
        
        BigDecimal calculatedTotal = calculatedSubtotal
            .add(deliveryFee != null ? deliveryFee : BigDecimal.ZERO)
            .subtract(discount != null ? discount : BigDecimal.ZERO);
            
        this.total = calculatedTotal;
        return calculatedTotal;
    }
    
    public BigDecimal getTotalPaidAmount() {
        return payments != null ? payments.stream()
            .filter(payment -> payment.getStatus() == Payment.PaymentStatus.APPROVED)
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;
    }
    
    public boolean isFullyPaid() {
        return getTotalPaidAmount().compareTo(total) >= 0;
    }
}