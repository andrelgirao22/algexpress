package br.com.alg.algexpress.domain.delivery;

import br.com.alg.algexpress.domain.order.Order;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Delivery {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_person_id")
    private DeliveryPerson deliveryPerson;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;
    
    @Column(name = "departure_time")
    private LocalDateTime departureTime;
    
    @Column(name = "delivery_time")
    private LocalDateTime deliveryTime;
    
    @Column(name = "return_time")
    private LocalDateTime returnTime;
    
    @Column(name = "distance_km", precision = 8, scale = 3)
    private BigDecimal distanceKm;
    
    @Column(name = "delivery_fee", precision = 8, scale = 2)
    private BigDecimal deliveryFee;
    
    @Column(length = 300)
    private String observations;
    
    @Column(name = "delivery_attempts", nullable = false)
    private Integer deliveryAttempts = 0;
    
    @Column(name = "cancellation_reason", length = 200)
    private String cancellationReason;
    
    @PrePersist
    private void prePersist() {
        if (status == null) {
            status = DeliveryStatus.WAITING_DELIVERY_PERSON;
        }
    }
    
    public enum DeliveryStatus {
        WAITING_DELIVERY_PERSON,
        EN_ROUTE,
        DELIVERY_ATTEMPT,
        DELIVERED,
        CANCELLED,
        RETURNED
    }
    
    public Integer calculateDeliveryTimeMinutes() {
        if (departureTime != null && deliveryTime != null) {
            return (int) Duration.between(departureTime, deliveryTime).toMinutes();
        }
        return null;
    }
    
    public Integer calculateTotalTimeMinutes() {
        if (departureTime != null && returnTime != null) {
            return (int) Duration.between(departureTime, returnTime).toMinutes();
        }
        return null;
    }
    
    public void incrementDeliveryAttempts() {
        this.deliveryAttempts = (this.deliveryAttempts != null ? this.deliveryAttempts : 0) + 1;
    }
}