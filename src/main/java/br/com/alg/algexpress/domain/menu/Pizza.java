package br.com.alg.algexpress.domain.menu;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "pizzas")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pizza {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(length = 300)
    private String description;
    
    @Column(name = "price_small", precision = 8, scale = 2)
    private BigDecimal priceSmall;
    
    @Column(name = "price_medium", precision = 8, scale = 2)
    private BigDecimal priceMedium;
    
    @Column(name = "price_large", precision = 8, scale = 2)
    private BigDecimal priceLarge;
    
    @Column(name = "price_extra_large", precision = 8, scale = 2)
    private BigDecimal priceExtraLarge;
    
    @ManyToMany
    @JoinTable(
        name = "pizza_ingredients",
        joinColumns = @JoinColumn(name = "pizza_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> ingredients;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PizzaCategory category;
    
    @Column(nullable = false)
    private Boolean available = true;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "preparation_time_minutes")
    private Integer preparationTimeMinutes;
    
    public enum PizzaCategory {
        TRADITIONAL,
        SPECIAL,
        PREMIUM,
        SWEET,
        VEGAN,
        LACTOSE_FREE
    }
    
    public enum PizzaSize {
        SMALL, MEDIUM, LARGE, EXTRA_LARGE
    }
    
    public BigDecimal getPriceBySize(PizzaSize size) {
        return switch (size) {
            case SMALL -> priceSmall;
            case MEDIUM -> priceMedium;
            case LARGE -> priceLarge;
            case EXTRA_LARGE -> priceExtraLarge;
        };
    }
}