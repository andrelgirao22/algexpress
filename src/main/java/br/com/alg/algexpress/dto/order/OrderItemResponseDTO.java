package br.com.alg.algexpress.dto.order;

import br.com.alg.algexpress.domain.menu.Pizza.PizzaSize;
import br.com.alg.algexpress.domain.order.OrderItem;
import br.com.alg.algexpress.dto.menu.PizzaResponseDTO;
import br.com.alg.algexpress.dto.menu.IngredientResponseDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderItemResponseDTO {
    
    private Long id;
    private PizzaResponseDTO pizza;
    private PizzaSize size;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private List<IngredientResponseDTO> additionalIngredients;
    private List<IngredientResponseDTO> removedIngredients;
    private String observations;
    
    public static OrderItemResponseDTO fromEntity(OrderItem orderItem) {
        OrderItemResponseDTO dto = new OrderItemResponseDTO();
        dto.setId(orderItem.getId());
        dto.setPizza(PizzaResponseDTO.fromEntity(orderItem.getPizza()));
        dto.setSize(orderItem.getSize());
        dto.setQuantity(orderItem.getQuantity());
        dto.setUnitPrice(orderItem.getUnitPrice());
        dto.setSubtotal(orderItem.getSubtotal());
        dto.setObservations(orderItem.getObservations());
        
        if (orderItem.getAdditionalIngredients() != null) {
            dto.setAdditionalIngredients(orderItem.getAdditionalIngredients().stream()
                .map(IngredientResponseDTO::fromEntity)
                .collect(Collectors.toList()));
        }
        
        if (orderItem.getRemovedIngredients() != null) {
            dto.setRemovedIngredients(orderItem.getRemovedIngredients().stream()
                .map(IngredientResponseDTO::fromEntity)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }
}