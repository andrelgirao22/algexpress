package br.com.alg.algexpress.infra.repository.menu;

import br.com.alg.algexpress.domain.menu.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    
    List<Pizza> findByAvailableTrue();
    
    List<Pizza> findByCategory(Pizza.PizzaCategory category);
    
    List<Pizza> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT p FROM Pizza p WHERE p.available = true AND p.category = :category")
    List<Pizza> findAvailablePizzasByCategory(@Param("category") Pizza.PizzaCategory category);
    
    @Query("SELECT p FROM Pizza p WHERE p.available = true ORDER BY p.name ASC")
    List<Pizza> findAllAvailablePizzasOrderByName();
    
    @Query("SELECT p FROM Pizza p WHERE " +
           "(:size = 'SMALL' AND p.priceSmall BETWEEN :minPrice AND :maxPrice) OR " +
           "(:size = 'MEDIUM' AND p.priceMedium BETWEEN :minPrice AND :maxPrice) OR " +
           "(:size = 'LARGE' AND p.priceLarge BETWEEN :minPrice AND :maxPrice) OR " +
           "(:size = 'EXTRA_LARGE' AND p.priceExtraLarge BETWEEN :minPrice AND :maxPrice)")
    List<Pizza> findByPriceRangeForSize(@Param("size") String size, 
                                        @Param("minPrice") BigDecimal minPrice, 
                                        @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT p FROM Pizza p JOIN p.ingredients i WHERE i.id = :ingredientId")
    List<Pizza> findPizzasWithIngredient(@Param("ingredientId") Long ingredientId);
    
    @Query("SELECT p FROM Pizza p WHERE p.preparationTimeMinutes <= :maxTime ORDER BY p.preparationTimeMinutes ASC")
    List<Pizza> findByMaxPreparationTime(@Param("maxTime") Integer maxTime);
    
    @Query("SELECT COUNT(p) FROM Pizza p WHERE p.available = true")
    Long countAvailablePizzas();
    
    @Query("SELECT COUNT(p) FROM Pizza p WHERE p.category = :category AND p.available = true")
    Long countAvailablePizzasByCategory(@Param("category") Pizza.PizzaCategory category);
    
    Optional<Pizza> findByNameIgnoreCase(String name);
    
    // Additional methods used by MenuService
    
    List<Pizza> findByAvailable(Boolean available);
    
    @Query("SELECT p FROM Pizza p WHERE " +
           "(:size = 'SMALL' AND p.priceSmall BETWEEN :minPrice AND :maxPrice) OR " +
           "(:size = 'MEDIUM' AND p.priceMedium BETWEEN :minPrice AND :maxPrice) OR " +
           "(:size = 'LARGE' AND p.priceLarge BETWEEN :minPrice AND :maxPrice) OR " +
           "(:size = 'EXTRA_LARGE' AND p.priceExtraLarge BETWEEN :minPrice AND :maxPrice)")
    List<Pizza> findByPriceBetween(@Param("minPrice") BigDecimal minPrice, 
                                   @Param("maxPrice") BigDecimal maxPrice, 
                                   @Param("size") Pizza.PizzaSize size);
    
    @Query("SELECT p FROM Pizza p LEFT JOIN OrderItem oi ON p.id = oi.pizza.id " +
           "GROUP BY p.id ORDER BY COUNT(oi) DESC")
    List<Pizza> findMostPopularPizzas();
    
    @Query("SELECT p FROM Pizza p WHERE p.category = 'VEGAN' AND p.available = true")
    List<Pizza> findVegetarianPizzas();
    
    @Query("SELECT COUNT(p) FROM Pizza p WHERE p.available = :available")
    Long countByAvailable(@Param("available") Boolean available);
    
    @Query("SELECT p.category, COUNT(p) FROM Pizza p WHERE p.available = true " +
           "GROUP BY p.category ORDER BY COUNT(p) DESC")
    List<Object[]> findPizzaStatisticsByCategory();
}