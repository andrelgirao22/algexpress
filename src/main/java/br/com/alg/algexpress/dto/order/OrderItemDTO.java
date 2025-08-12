package br.com.alg.algexpress.dto.order;

import br.com.alg.algexpress.domain.menu.Pizza;
import br.com.alg.algexpress.domain.order.OrderItem;
import br.com.alg.algexpress.dto.menu.IngredientDTO;
import br.com.alg.algexpress.dto.menu.PizzaDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record OrderItemDTO(
    Long id,
    
    @NotNull(message = "Pizza ID is required")
    Long pizzaId,
    
    PizzaDTO pizza,
    
    @NotNull(message = "Pizza size is required")
    Pizza.PizzaSize size,
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    Integer quantity,
    
    BigDecimal unitPrice,
    BigDecimal subtotal,
    List<Long> additionalIngredientIds,
    List<Long> removedIngredientIds,
    List<IngredientDTO> additionalIngredients,
    List<IngredientDTO> removedIngredients,
    String observations
) {
    
    public static OrderItemDTO fromEntity(OrderItem orderItem) {
        List<IngredientDTO> additionalIngredients = null;
        List<IngredientDTO> removedIngredients = null;
        
        if (orderItem.getAdditionalIngredients() != null) {
            additionalIngredients = orderItem.getAdditionalIngredients().stream()
                .map(IngredientDTO::fromEntity)
                .toList();
        }
        
        if (orderItem.getRemovedIngredients() != null) {
            removedIngredients = orderItem.getRemovedIngredients().stream()
                .map(IngredientDTO::fromEntity)
                .toList();
        }
        
        return new OrderItemDTO(
            orderItem.getId(),
            orderItem.getPizza() != null ? orderItem.getPizza().getId() : null,
            orderItem.getPizza() != null ? PizzaDTO.fromEntity(orderItem.getPizza()) : null,
            orderItem.getSize(),
            orderItem.getQuantity(),
            orderItem.getUnitPrice(),
            orderItem.getTotalPrice(),
            null,
            null,
            additionalIngredients,
            removedIngredients,
            orderItem.getObservations()
        );
    }
}