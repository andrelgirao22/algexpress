package br.com.alg.algexpress.dto.order;

import br.com.alg.algexpress.domain.order.Order;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    @NotNull(message = "Order type is required")
    private Order.OrderType type;
    
    private Long addressId;
    
    @Size(max = 500, message = "Observations must not exceed 500 characters")
    private String observations;
    
    @NotEmpty(message = "Order must have at least one item")
    @Valid
    private List<OrderItemRequestDTO> items;
}