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
    
    @Size(max = 20, message = "License plate must not exceed 20 characters")
    String vehicleLicensePlate,
    
    @Size(max = 50, message = "Vehicle model must not exceed 50 characters")
    String vehicleModel,
    
    DeliveryPerson.DeliveryPersonStatus status,
    Boolean isAvailable
) {

    
    public static DeliveryPersonDTO fromEntity(DeliveryPerson deliveryPerson) {
        return new DeliveryPersonDTO(
            deliveryPerson.getId(),
            deliveryPerson.getName(),
            deliveryPerson.getPhone(),
            deliveryPerson.getVehicleLicensePlate(),
            deliveryPerson.getVehicleModel(),
            deliveryPerson.getStatus(),
            deliveryPerson.getIsAvailable()
        );
    }
    
    public DeliveryPerson toEntity() {
        DeliveryPerson deliveryPerson = new DeliveryPerson();
        deliveryPerson.setId(this.id);
        deliveryPerson.setName(this.name);
        deliveryPerson.setPhone(this.phone);
        deliveryPerson.setVehicleLicensePlate(this.vehicleLicensePlate);
        deliveryPerson.setVehicleModel(this.vehicleModel);
        deliveryPerson.setStatus(this.status);
        deliveryPerson.setIsAvailable(this.isAvailable);
        return deliveryPerson;
    }
}