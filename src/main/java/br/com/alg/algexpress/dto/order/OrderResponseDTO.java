package br.com.alg.algexpress.dto.order;

import br.com.alg.algexpress.domain.order.Order;
import br.com.alg.algexpress.dto.customer.CustomerDTO;
import br.com.alg.algexpress.dto.customer.AddressDTO;
import br.com.alg.algexpress.dto.delivery.DeliveryDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderResponseDTO {
    
    private Long id;
    private CustomerDTO customer;
    private Order.OrderType type;
    private Order.OrderStatus status;
    private AddressDTO address;
    private LocalDateTime orderDateTime;
    private LocalDateTime confirmationTime;
    private BigDecimal total;
    private BigDecimal deliveryFee;
    private String observations;
    private List<OrderItemResponseDTO> items;
    private DeliveryDTO delivery;
    
    public static OrderResponseDTO fromEntity(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setCustomer(CustomerDTO.fromEntity(order.getCustomer()));
        dto.setType(order.getType());
        dto.setStatus(order.getStatus());
        
        if (order.getAddress() != null) {
            dto.setAddress(AddressDTO.fromEntity(order.getAddress()));
        }
        
        dto.setOrderDateTime(order.getOrderDateTime());
        dto.setConfirmationTime(order.getConfirmationTime());
        dto.setTotal(order.getTotal());
        dto.setDeliveryFee(order.getDeliveryFee());
        dto.setObservations(order.getObservations());
        
        if (order.getOrderItems() != null) {
            dto.setItems(order.getOrderItems().stream()
                .map(OrderItemResponseDTO::fromEntity)
                .collect(Collectors.toList()));
        }
        
        if (order.getDelivery() != null) {
            dto.setDelivery(DeliveryDTO.fromEntity(order.getDelivery()));
        }
        
        return dto;
    }
}