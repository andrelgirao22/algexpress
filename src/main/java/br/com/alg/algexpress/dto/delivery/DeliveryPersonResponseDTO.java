package br.com.alg.algexpress.dto.delivery;

import br.com.alg.algexpress.domain.delivery.DeliveryPerson;
import lombok.Data;

@Data
public class DeliveryPersonResponseDTO {
    
    private Long id;
    private String name;
    private String phone;
    private String vehicleLicensePlate;
    private String vehicleModel;
    private DeliveryPerson.DeliveryPersonStatus status;
    private Boolean isAvailable;
    
    public static DeliveryPersonResponseDTO fromEntity(DeliveryPerson deliveryPerson) {
        DeliveryPersonResponseDTO dto = new DeliveryPersonResponseDTO();
        dto.setId(deliveryPerson.getId());
        dto.setName(deliveryPerson.getName());
        dto.setPhone(deliveryPerson.getPhone());
        dto.setVehicleLicensePlate(deliveryPerson.getVehicleLicensePlate());
        dto.setVehicleModel(deliveryPerson.getVehicleModel());
        dto.setStatus(deliveryPerson.getStatus());
        dto.setIsAvailable(deliveryPerson.getIsAvailable());
        return dto;
    }
}