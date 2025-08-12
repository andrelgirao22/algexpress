package br.com.alg.algexpress.dto.order;

import br.com.alg.algexpress.domain.menu.Pizza;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderItemRequestDTO {
    
    @NotNull(message = "Pizza ID is required")
    private Long pizzaId;
    
    @NotNull(message = "Pizza size is required")
    private Pizza.PizzaSize size;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    private List<Long> additionalIngredients;
    private List<Long> removedIngredients;
    private String observations;
}