package br.com.alg.algexpress.dto.delivery;

import br.com.alg.algexpress.domain.delivery.Delivery;

import java.time.LocalDateTime;

public record DeliveryDTO(
    Long id,
    Long orderId,
    Long deliveryPersonId,
    String deliveryPersonName,
    Delivery.DeliveryStatus status,
    LocalDateTime departureTime,
    LocalDateTime deliveryTime,
    LocalDateTime returnTime,
    String observations,
    Integer deliveryAttempts,
    String cancellationReason
) {
    
    public static DeliveryDTO fromEntity(Delivery delivery) {
        return new DeliveryDTO(
            delivery.getId(),
            delivery.getOrder() != null ? delivery.getOrder().getId() : null,
            delivery.getDeliveryPerson() != null ? delivery.getDeliveryPerson().getId() : null,
            delivery.getDeliveryPerson() != null ? delivery.getDeliveryPerson().getName() : null,
            delivery.getStatus(),
            delivery.getDepartureTime(),
            delivery.getDeliveryTime(),
            delivery.getReturnTime(),
            delivery.getObservations(),
            delivery.getDeliveryAttempts(),
            delivery.getCancellationReason()
        );
    }
}