package br.com.alg.algexpress.domain.order;

import br.com.alg.algexpress.domain.menu.Ingredient;
import br.com.alg.algexpress.domain.menu.Pizza;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "order_items")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pizza_id", nullable = false)
    private Pizza pizza;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Pizza.PizzaSize size;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @ManyToMany
    @JoinTable(
        name = "order_item_additional_ingredients",
        joinColumns = @JoinColumn(name = "order_item_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> additionalIngredients;
    
    @ManyToMany
    @JoinTable(
        name = "order_item_removed_ingredients",
        joinColumns = @JoinColumn(name = "order_item_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> removedIngredients;
    
    @Column(name = "unit_price", precision = 8, scale = 2, nullable = false)
    private BigDecimal unitPrice;
    
    @Column(name = "total_price", precision = 8, scale = 2, nullable = false)
    private BigDecimal totalPrice;
    
    @Column(length = 300)
    private String observations;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    public BigDecimal calculateTotalPrice() {
        BigDecimal basePrice = pizza.getPriceBySize(size);
        BigDecimal additionalIngredientsCost = BigDecimal.ZERO;
        
        if (additionalIngredients != null) {
            additionalIngredientsCost = additionalIngredients.stream()
                .map(ingredient -> ingredient.getAdditionalPrice() != null ? ingredient.getAdditionalPrice() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        
        return basePrice.add(additionalIngredientsCost).multiply(BigDecimal.valueOf(quantity));
    }
    
    @PrePersist
    @PreUpdate
    private void calculatePrices() {
        this.unitPrice = pizza.getPriceBySize(size);
        this.totalPrice = calculateTotalPrice();
    }
}