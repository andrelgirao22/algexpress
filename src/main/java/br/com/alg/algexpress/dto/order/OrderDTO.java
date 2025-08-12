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
    LocalDateTime confirmationTime,
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
        CustomerDTO customerDTO = CustomerDTO.fromEntity(order.getCustomer());
        
        AddressDTO addressDTO = null;
        if (order.getAddress() != null) {
            addressDTO = AddressDTO.fromEntity(order.getAddress());
        }
        
        List<OrderItemDTO> itemsDTO = null;
        if (order.getOrderItems() != null) {
            itemsDTO = order.getOrderItems().stream()
                .map(OrderItemDTO::fromEntity)
                .toList();
        }
        
        DeliveryDTO deliveryDTO = null;
        if (order.getDelivery() != null) {
            deliveryDTO = DeliveryDTO.fromEntity(order.getDelivery());
        }
        
        return new OrderDTO(
            order.getId(),
            order.getCustomer().getId(),
            customerDTO,
            order.getType(),
            order.getStatus(),
            order.getAddress() != null ? order.getAddress().getId() : null,
            addressDTO,
            order.getOrderDateTime(),
            order.getConfirmationTime(),
            order.getTotal(),
            order.getDeliveryFee(),
            order.getObservations(),
            itemsDTO,
            deliveryDTO
        );
    }
}