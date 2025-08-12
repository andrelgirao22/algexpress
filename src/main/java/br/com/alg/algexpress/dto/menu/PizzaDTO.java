package br.com.alg.algexpress.dto.menu;

import br.com.alg.algexpress.domain.menu.Pizza;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record PizzaDTO(
    Long id,
    
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    String name,
    
    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    String description,
    
    @NotNull(message = "Category is required")
    Pizza.PizzaCategory category,
    
    @NotNull(message = "Small price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    BigDecimal priceSmall,
    
    @NotNull(message = "Medium price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    BigDecimal priceMedium,
    
    @NotNull(message = "Large price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    BigDecimal priceLarge,
    
    BigDecimal priceExtraLarge,
    
    Boolean available,
    String imageUrl,
    Integer preparationTimeMinutes,
    List<IngredientDTO> ingredients
) {
    
    public static PizzaDTO fromEntity(Pizza pizza) {
        List<IngredientDTO> ingredientDTOs = null;
        if (pizza.getIngredients() != null) {
            ingredientDTOs = pizza.getIngredients().stream()
                .map(IngredientDTO::fromEntity)
                .toList();
        }
        
        return new PizzaDTO(
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
            pizza.getPreparationTimeMinutes(),
            ingredientDTOs
        );
    }
    
    public Pizza toEntity() {
        Pizza pizza = new Pizza();
        pizza.setId(this.id);
        pizza.setName(this.name);
        pizza.setDescription(this.description);
        pizza.setCategory(this.category);
        pizza.setPriceSmall(this.priceSmall);
        pizza.setPriceMedium(this.priceMedium);
        pizza.setPriceLarge(this.priceLarge);
        pizza.setPriceExtraLarge(this.priceExtraLarge);
        pizza.setAvailable(this.available != null ? this.available : true);
        pizza.setImageUrl(this.imageUrl);
        pizza.setPreparationTimeMinutes(this.preparationTimeMinutes);
        return pizza;
    }
}