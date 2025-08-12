package br.com.alg.algexpress.dto.delivery;

import br.com.alg.algexpress.domain.delivery.Delivery;

import java.time.LocalDateTime;

public record DeliveryDTO(
    Long id,
    Long orderId,
    Long deliveryPersonId,
    String deliveryPersonName,
    Delivery.DeliveryStatus status,
    LocalDateTime assignedTime,
    LocalDateTime departureTime,
    LocalDateTime deliveryTime,
    String trackingNotes
) {
    
    public static DeliveryDTO fromEntity(Delivery delivery) {
        return new DeliveryDTO(
            delivery.getId(),
            delivery.getOrder().getId(),
            delivery.getDeliveryPerson() != null ? delivery.getDeliveryPerson().getId() : null,
            delivery.getDeliveryPerson() != null ? delivery.getDeliveryPerson().getName() : null,
            delivery.getStatus(),
            delivery.getAssignedTime(),
            delivery.getDepartureTime(),
            delivery.getDeliveryTime(),
            delivery.getTrackingNotes()
        );
    }
}