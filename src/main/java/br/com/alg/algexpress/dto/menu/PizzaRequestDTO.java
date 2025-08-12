package br.com.alg.algexpress.dto.menu;

import br.com.alg.algexpress.domain.menu.Pizza;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PizzaRequestDTO {
    
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;
    
    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    @NotNull(message = "Category is required")
    private Pizza.PizzaCategory category;
    
    @NotNull(message = "Small price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    private BigDecimal priceSmall;
    
    @NotNull(message = "Medium price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    private BigDecimal priceMedium;
    
    @NotNull(message = "Large price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    private BigDecimal priceLarge;
    
    @NotNull(message = "Family price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    private BigDecimal priceFamily;
    
    private Boolean isVegetarian = false;
    private Boolean isAvailable = true;
    private List<Long> ingredientIds;
}