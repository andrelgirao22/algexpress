package br.com.alg.algexpress.dto.delivery;

import br.com.alg.algexpress.domain.delivery.Delivery;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeliveryResponseDTO {
    
    private Long id;
    private Long orderId;
    private Long deliveryPersonId;
    private String deliveryPersonName;
    private Delivery.DeliveryStatus status;
    private LocalDateTime assignedTime;
    private LocalDateTime departureTime;
    private LocalDateTime deliveryTime;
    private String trackingNotes;
    
    public static DeliveryResponseDTO fromEntity(Delivery delivery) {
        DeliveryResponseDTO dto = new DeliveryResponseDTO();
        dto.setId(delivery.getId());
        dto.setOrderId(delivery.getOrder().getId());
        
        if (delivery.getDeliveryPerson() != null) {
            dto.setDeliveryPersonId(delivery.getDeliveryPerson().getId());
            dto.setDeliveryPersonName(delivery.getDeliveryPerson().getName());
        }
        
        dto.setStatus(delivery.getStatus());
        dto.setAssignedTime(delivery.getAssignedTime());
        dto.setDepartureTime(delivery.getDepartureTime());
        dto.setDeliveryTime(delivery.getDeliveryTime());
        dto.setTrackingNotes(delivery.getTrackingNotes());
        
        return dto;
    }
}