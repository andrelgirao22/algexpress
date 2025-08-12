package br.com.alg.algexpress.dto.menu;

import br.com.alg.algexpress.domain.menu.Pizza;
import br.com.alg.algexpress.dto.menu.IngredientResponseDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PizzaResponseDTO {
    
    private Long id;
    private String name;
    private String description;
    private Pizza.PizzaCategory category;
    private BigDecimal priceSmall;
    private BigDecimal priceMedium;
    private BigDecimal priceLarge;
    private BigDecimal priceFamily;
    private Boolean isVegetarian;
    private Boolean isAvailable;
    private List<IngredientResponseDTO> ingredients;
    
    public static PizzaResponseDTO fromEntity(Pizza pizza) {
        PizzaResponseDTO dto = new PizzaResponseDTO();
        dto.setId(pizza.getId());
        dto.setName(pizza.getName());
        dto.setDescription(pizza.getDescription());
        dto.setCategory(pizza.getCategory());
        dto.setPriceSmall(pizza.getPriceSmall());
        dto.setPriceMedium(pizza.getPriceMedium());
        dto.setPriceLarge(pizza.getPriceLarge());
        dto.setPriceFamily(pizza.getPriceFamily());
        dto.setIsVegetarian(pizza.getIsVegetarian());
        dto.setIsAvailable(pizza.getIsAvailable());
        
        if (pizza.getIngredients() != null) {
            dto.setIngredients(pizza.getIngredients().stream()
                .map(IngredientResponseDTO::fromEntity)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }
}