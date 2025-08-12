package br.com.alg.algexpress.dto.order;

import br.com.alg.algexpress.domain.order.Order;
import br.com.alg.algexpress.dto.customer.AddressDTO;
import br.com.alg.algexpress.dto.customer.CustomerDTO;
import br.com.alg.algexpress.dto.delivery.DeliveryDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(
    Long id,
    
    @NotNull(message = "Customer ID is required")
    Long customerId,
    
    CustomerDTO customer,
    
    @NotNull(message = "Order type is required")
    Order.OrderType type,
    
    Order.OrderStatus status,
    Long addressId,
    AddressDTO address,
    LocalDateTime orderDateTime,
    LocalDateTime estimatedDateTime,
    LocalDateTime completionDateTime,
    BigDecimal total,
    BigDecimal deliveryFee,
    
    @Size(max = 500, message = "Observations must not exceed 500 characters")
    String observations,
    
    @NotEmpty(message = "Order must have at least one item")
    @Valid
    List<OrderItemDTO> items,
    
    DeliveryDTO delivery
) {
    
    public static OrderDTO fromEntity(Order order) {
        CustomerDTO customerDTO = order.getCustomer() != null ? CustomerDTO.fromEntity(order.getCustomer()) : null;
        
        AddressDTO addressDTO = null;
        if (order.getDeliveryAddress() != null) {
            addressDTO = AddressDTO.fromEntity(order.getDeliveryAddress());
        }
        
        List<OrderItemDTO> itemsDTO = null;
        if (order.getItems() != null) {
            itemsDTO = order.getItems().stream()
                .map(OrderItemDTO::fromEntity)
                .toList();
        }
        
        // Delivery information would need to be retrieved separately
        DeliveryDTO deliveryDTO = null;
        
        return new OrderDTO(
            order.getId(),
            order.getCustomer() != null ? order.getCustomer().getId() : null,
            customerDTO,
            order.getType(),
            order.getStatus(),
            order.getDeliveryAddress() != null ? order.getDeliveryAddress().getId() : null,
            addressDTO,
            order.getOrderDateTime(),
            order.getEstimatedDateTime(),
            order.getCompletionDateTime(),
            order.getTotal(),
            order.getDeliveryFee(),
            order.getObservations(),
            itemsDTO,
            deliveryDTO
        );
    }
}