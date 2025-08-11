package br.com.alg.algexpress.domain.delivery;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_persons")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DeliveryPerson {
    
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
    
    @Column(nullable = false, length = 20, unique = true)
    private String document;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryPersonStatus status;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;
    
    @Column(name = "vehicle_plate", length = 10)
    private String vehiclePlate;
    
    @Column(name = "shift_start")
    private LocalDateTime shiftStart;
    
    @Column(name = "shift_end")
    private LocalDateTime shiftEnd;
    
    @Column(nullable = false)
    private Boolean available = true;
    
    @PrePersist
    private void prePersist() {
        if (status == null) {
            status = DeliveryPersonStatus.ACTIVE;
        }
    }
    
    public enum DeliveryPersonStatus {
        ACTIVE,
        INACTIVE,
        ON_DELIVERY,
        ON_BREAK
    }
    
    public enum VehicleType {
        MOTORCYCLE,
        CAR,
        BICYCLE,
        ON_FOOT
    }
}