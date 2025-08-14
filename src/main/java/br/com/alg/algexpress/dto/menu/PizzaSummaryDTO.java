package br.com.alg.algexpress.dto.menu;

import br.com.alg.algexpress.domain.menu.Pizza;

import java.math.BigDecimal;

public record PizzaSummaryDTO(
    Long id,
    String name,
    String description,
    Pizza.PizzaCategory category,
    BigDecimal priceSmall,
    BigDecimal priceMedium, 
    BigDecimal priceLarge,
    BigDecimal priceExtraLarge,
    Boolean available,
    String imageUrl,
    Integer preparationTimeMinutes
) {
    
    public static PizzaSummaryDTO fromEntity(Pizza pizza) {
        return new PizzaSummaryDTO(
            pizza.getId(),
            pizza.getName(),
            pizza.getDescription(),
            pizza.getCategory(),
            pizza.getPriceSmall(),
            pizza.getPriceMedium(),
            pizza.getPriceLarge(),
            pizza.getPriceExtraLarge(),
            pizza.getAvailable(),
            pizza.getImageUrl(),
            pizza.getPreparationTimeMinutes()
        );
    }
}