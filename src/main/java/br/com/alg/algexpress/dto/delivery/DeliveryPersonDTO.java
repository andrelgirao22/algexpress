package br.com.alg.algexpress.dto.delivery;

import br.com.alg.algexpress.domain.delivery.DeliveryPerson;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DeliveryPersonDTO(
    Long id,
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    String name,
    
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone format is invalid")
    @Size(max = 15, message = "Phone must not exceed 15 characters")
    String phone,
    
    @Size(max = 100, message = "Email must not exceed 100 characters")
    String email,
    
    @Size(max = 20, message = "Document must not exceed 20 characters")
    String document,
    
    DeliveryPerson.DeliveryPersonStatus status,
    
    DeliveryPerson.VehicleType vehicleType,
    
    @Size(max = 10, message = "Vehicle plate must not exceed 10 characters")
    String vehiclePlate,
    
    Boolean available
) {

    
    public static DeliveryPersonDTO fromEntity(DeliveryPerson deliveryPerson) {
        return new DeliveryPersonDTO(
            deliveryPerson.getId(),
            deliveryPerson.getName(),
            deliveryPerson.getPhone(),
            deliveryPerson.getEmail(),
            deliveryPerson.getDocument(),
            deliveryPerson.getStatus(),
            deliveryPerson.getVehicleType(),
            deliveryPerson.getVehiclePlate(),
            deliveryPerson.getAvailable()
        );
    }
    
    public DeliveryPerson toEntity() {
        DeliveryPerson deliveryPerson = new DeliveryPerson();
        deliveryPerson.setId(this.id);
        deliveryPerson.setName(this.name);
        deliveryPerson.setPhone(this.phone);
        deliveryPerson.setEmail(this.email);
        deliveryPerson.setDocument(this.document);
        deliveryPerson.setStatus(this.status);
        deliveryPerson.setVehicleType(this.vehicleType);
        deliveryPerson.setVehiclePlate(this.vehiclePlate);
        deliveryPerson.setAvailable(this.available);
        return deliveryPerson;
    }
}