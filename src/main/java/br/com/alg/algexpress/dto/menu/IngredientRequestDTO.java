package br.com.alg.algexpress.dto.menu;

import br.com.alg.algexpress.domain.menu.Ingredient;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class IngredientRequestDTO {
    
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    @NotNull(message = "Category is required")
    private Ingredient.IngredientCategory category;
    
    @NotNull(message = "Additional price is required")
    @DecimalMin(value = "0.00", message = "Additional price must be zero or greater")
    private BigDecimal additionalPrice;
    
    private Boolean isVegetarian = true;
    private Boolean isAvailable = true;
}