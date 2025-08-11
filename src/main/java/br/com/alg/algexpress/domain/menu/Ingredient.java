package br.com.alg.algexpress.domain.menu;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity
@Table(name = "ingredients")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ingredient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(length = 200)
    private String description;
    
    @Column(name = "additional_price", precision = 8, scale = 2)
    private BigDecimal additionalPrice;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IngredientCategory category;
    
    @Column(nullable = false)
    private Boolean available = true;
    
    @Column(nullable = false)
    private Boolean allergenic = false;
    
    public enum IngredientCategory {
        PROTEIN,
        CHEESE,
        VEGETABLE,
        SAUCE,
        SEASONING,
        OTHER
    }
}