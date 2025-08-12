package br.com.alg.algexpress.dto.menu;

import br.com.alg.algexpress.domain.menu.Ingredient;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class IngredientResponseDTO {
    
    private Long id;
    private String name;
    private String description;
    private Ingredient.IngredientCategory category;
    private BigDecimal additionalPrice;
    private Boolean isVegetarian;
    private Boolean isAvailable;
    
    public static IngredientResponseDTO fromEntity(Ingredient ingredient) {
        IngredientResponseDTO dto = new IngredientResponseDTO();
        dto.setId(ingredient.getId());
        dto.setName(ingredient.getName());
        dto.setDescription(ingredient.getDescription());
        dto.setCategory(ingredient.getCategory());
        dto.setAdditionalPrice(ingredient.getAdditionalPrice());
        dto.setIsVegetarian(ingredient.getIsVegetarian());
        dto.setIsAvailable(ingredient.getIsAvailable());
        return dto;
    }
}