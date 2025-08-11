package br.com.alg.algexpress.domain.address;

import br.com.alg.algexpress.domain.customer.Customer;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity
@Table(name = "addresses")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class
Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String street;
    
    @Column(nullable = false, length = 10)
    private String number;
    
    @Column(length = 100)
    private String complement;
    
    @Column(nullable = false, length = 100)
    private String neighborhood;
    
    @Column(nullable = false, length = 100)
    private String city;
    
    @Column(nullable = false, length = 2)
    private String state;
    
    @Column(nullable = false, length = 10)
    private String zipCode;
    
    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;
    
    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;
    
    @Column(name = "distance_km", precision = 8, scale = 3)
    private BigDecimal distanceKm;
    
    @Column(name = "delivery_fee", precision = 8, scale = 2)
    private BigDecimal deliveryFee;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AddressType type;
    
    @Column(name = "reference_points", length = 200)
    private String referencePoints;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @PrePersist
    private void prePersist() {
        if (type == null) {
            type = AddressType.RESIDENTIAL;
        }
    }
    
    public enum AddressType {
        RESIDENTIAL,
        COMMERCIAL,
        OTHER
    }
}