package br.com.alg.algexpress.dto.order;

import br.com.alg.algexpress.domain.order.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderSummaryDTO(
    Long id,
    Long customerId,
    String customerName,
    Order.OrderType type,
    Order.OrderStatus status,
    LocalDateTime orderDateTime,
    LocalDateTime estimatedDateTime,
    BigDecimal total,
    BigDecimal deliveryFee,
    Integer itemCount
) {
    
    public static OrderSummaryDTO fromEntity(Order order) {
        String customerName = order.getCustomer() != null ? order.getCustomer().getName() : null;
        Integer itemCount = order.getItems() != null ? order.getItems().size() : 0;
        
        return new OrderSummaryDTO(
            order.getId(),
            order.getCustomer() != null ? order.getCustomer().getId() : null,
            customerName,
            order.getType(),
            order.getStatus(),
            order.getOrderDateTime(),
            order.getEstimatedDateTime(),
            order.getTotal(),
            order.getDeliveryFee(),
            itemCount
        );
    }
}