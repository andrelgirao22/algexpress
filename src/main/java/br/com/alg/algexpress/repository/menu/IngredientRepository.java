package br.com.alg.algexpress.repository.menu;

import br.com.alg.algexpress.domain.menu.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    
    List<Ingredient> findByAvailableTrue();
    
    List<Ingredient> findByCategory(Ingredient.IngredientCategory category);
    
    List<Ingredient> findByAllergenicTrue();
    
    List<Ingredient> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT i FROM Ingredient i WHERE i.available = true AND i.category = :category")
    List<Ingredient> findAvailableIngredientsByCategory(@Param("category") Ingredient.IngredientCategory category);
    
    @Query("SELECT i FROM Ingredient i WHERE i.available = true ORDER BY i.category ASC, i.name ASC")
    List<Ingredient> findAllAvailableIngredientsOrderByCategoryAndName();
    
    @Query("SELECT i FROM Ingredient i WHERE i.additionalPrice BETWEEN :minPrice AND :maxPrice")
    List<Ingredient> findByAdditionalPriceBetween(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT i FROM Ingredient i WHERE i.additionalPrice = 0 AND i.available = true")
    List<Ingredient> findFreeAvailableIngredients();
    
    @Query("SELECT i FROM Ingredient i WHERE i.additionalPrice > 0 AND i.available = true ORDER BY i.additionalPrice ASC")
    List<Ingredient> findPaidAvailableIngredients();
    
    @Query("SELECT COUNT(i) FROM Ingredient i WHERE i.available = true")
    Long countAvailableIngredients();
    
    @Query("SELECT COUNT(i) FROM Ingredient i WHERE i.category = :category AND i.available = true")
    Long countAvailableIngredientsByCategory(@Param("category") Ingredient.IngredientCategory category);
    
    @Query("SELECT DISTINCT i.category FROM Ingredient i WHERE i.available = true")
    List<Ingredient.IngredientCategory> findAvailableCategories();
    
    Optional<Ingredient> findByNameIgnoreCase(String name);
}