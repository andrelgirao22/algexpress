package br.com.alg.algexpress.domain.customer;

import br.com.alg.algexpress.domain.valueObjects.Address;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, length = 20)
    private String phone;
    
    @Column(length = 100)
    private String email;
    
    @Column(length = 14)
    private String cpf;
    
    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Address> addresses;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status;
    
    @Column(name = "loyalty_points", nullable = false)
    private Integer loyaltyPoints = 0;
    
    @PrePersist
    private void prePersist() {
        if (registrationDate == null) {
            registrationDate = LocalDateTime.now();
        }
        if (status == null) {
            status = CustomerStatus.ACTIVE;
        }
    }
    
    public enum CustomerStatus {
        ACTIVE,
        INACTIVE,
        BLOCKED
    }
}