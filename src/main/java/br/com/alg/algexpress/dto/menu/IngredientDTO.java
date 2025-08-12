package br.com.alg.algexpress.dto.menu;

import br.com.alg.algexpress.domain.menu.Ingredient;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record IngredientDTO(
    Long id,
    
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    String name,
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    String description,
    
    @NotNull(message = "Category is required")
    Ingredient.IngredientCategory category,
    
    @DecimalMin(value = "0.00", message = "Additional price must be zero or greater")
    BigDecimal additionalPrice,
    
    Boolean available,
    Boolean allergenic
) {
    
    public static IngredientDTO fromEntity(Ingredient ingredient) {
        return new IngredientDTO(
            ingredient.getId(),
            ingredient.getName(),
            ingredient.getDescription(),
            ingredient.getCategory(),
            ingredient.getAdditionalPrice(),
            ingredient.getAvailable(),
            ingredient.getAllergenic()
        );
    }
    
    public Ingredient toEntity() {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(this.id);
        ingredient.setName(this.name);
        ingredient.setDescription(this.description);
        ingredient.setCategory(this.category);
        ingredient.setAdditionalPrice(this.additionalPrice);
        ingredient.setAvailable(this.available != null ? this.available : true);
        ingredient.setAllergenic(this.allergenic != null ? this.allergenic : false);
        return ingredient;
    }
}