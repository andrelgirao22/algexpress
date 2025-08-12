package br.com.alg.algexpress.infra.service;

import br.com.alg.algexpress.domain.menu.Ingredient;
import br.com.alg.algexpress.domain.menu.Pizza;
import br.com.alg.algexpress.infra.repository.menu.IngredientRepository;
import br.com.alg.algexpress.infra.repository.menu.PizzaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MenuService {

    private final PizzaRepository pizzaRepository;
    private final IngredientRepository ingredientRepository;

    public MenuService(PizzaRepository pizzaRepository, IngredientRepository ingredientRepository) {
        this.pizzaRepository = pizzaRepository;
        this.ingredientRepository = ingredientRepository;
    }

    // === PIZZA METHODS ===

    @Transactional(readOnly = true)
    public Optional<Pizza> findPizzaById(Long id) {
        return pizzaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Pizza> findAllPizzas() {
        return pizzaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Pizza> findAvailablePizzas() {
        return pizzaRepository.findByAvailable(true);
    }

    @Transactional(readOnly = true)
    public List<Pizza> findPizzasByCategory(Pizza.PizzaCategory category) {
        return pizzaRepository.findByCategory(category);
    }

    @Transactional(readOnly = true)
    public List<Pizza> findPizzasByNameContaining(String name) {
        return pizzaRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public List<Pizza> findPizzasByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pizza.PizzaSize size) {
        return pizzaRepository.findByPriceBetween(minPrice, maxPrice, size);
    }

    @Transactional(readOnly = true)
    public List<Pizza> findMostPopularPizzas() {
        return pizzaRepository.findMostPopularPizzas();
    }

    @Transactional(readOnly = true)
    public List<Pizza> findPizzasWithIngredient(Long ingredientId) {
        return pizzaRepository.findPizzasWithIngredient(ingredientId);
    }

    @Transactional(readOnly = true)
    public List<Pizza> findVegetarianPizzas() {
        return pizzaRepository.findVegetarianPizzas();
    }

    public Pizza savePizza(Pizza pizza) {
        return pizzaRepository.save(pizza);
    }

    public Pizza updatePizzaAvailability(Long pizzaId, boolean isAvailable) {
        Optional<Pizza> pizzaOpt = pizzaRepository.findById(pizzaId);
        if (pizzaOpt.isPresent()) {
            Pizza pizza = pizzaOpt.get();
            pizza.setAvailable(isAvailable);
            return pizzaRepository.save(pizza);
        }
        throw new RuntimeException("Pizza not found with id: " + pizzaId);
    }

    public Pizza updatePizzaPrice(Long pizzaId, Pizza.PizzaSize size, BigDecimal newPrice) {
        Optional<Pizza> pizzaOpt = pizzaRepository.findById(pizzaId);
        if (pizzaOpt.isPresent()) {
            Pizza pizza = pizzaOpt.get();
            switch (size) {
                case SMALL -> pizza.setPriceSmall(newPrice);
                case MEDIUM -> pizza.setPriceMedium(newPrice);
                case LARGE -> pizza.setPriceLarge(newPrice);
                case EXTRA_LARGE -> pizza.setPriceExtraLarge(newPrice);
            }
            return pizzaRepository.save(pizza);
        }
        throw new RuntimeException("Pizza not found with id: " + pizzaId);
    }

    @Transactional(readOnly = true)
    public BigDecimal getPizzaPrice(Long pizzaId, Pizza.PizzaSize size) {
        Optional<Pizza> pizzaOpt = pizzaRepository.findById(pizzaId);
        if (pizzaOpt.isPresent()) {
            Pizza pizza = pizzaOpt.get();
            return switch (size) {
                case SMALL -> pizza.getPriceSmall();
                case MEDIUM -> pizza.getPriceMedium();
                case LARGE -> pizza.getPriceLarge();
                case EXTRA_LARGE -> pizza.getPriceExtraLarge();
            };
        }
        throw new RuntimeException("Pizza not found with id: " + pizzaId);
    }

    public void deletePizza(Long pizzaId) {
        pizzaRepository.deleteById(pizzaId);
    }

    // === INGREDIENT METHODS ===

    @Transactional(readOnly = true)
    public Optional<Ingredient> findIngredientById(Long id) {
        return ingredientRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Ingredient> findAllIngredients() {
        return ingredientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Ingredient> findAvailableIngredients() {
        return ingredientRepository.findByAvailable(true);
    }

    @Transactional(readOnly = true)
    public List<Ingredient> findIngredientsByCategory(Ingredient.IngredientCategory category) {
        return ingredientRepository.findByCategory(category);
    }

    @Transactional(readOnly = true)
    public List<Ingredient> findIngredientsByNameContaining(String name) {
        return ingredientRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public List<Ingredient> findVegetarianIngredients() {
        // Note: vegetarian field doesn't exist in Ingredient entity
        // Would need to be implemented based on category or other criteria
        return ingredientRepository.findByCategory(Ingredient.IngredientCategory.VEGETABLE);
    }

    @Transactional(readOnly = true)
    public List<Ingredient> findIngredientsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return ingredientRepository.findByAdditionalPriceBetween(minPrice, maxPrice);
    }

    @Transactional(readOnly = true)
    public List<Ingredient> findMostUsedIngredients() {
        return ingredientRepository.findMostUsedIngredients();
    }

    public Ingredient saveIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public Ingredient updateIngredientAvailability(Long ingredientId, boolean isAvailable) {
        Optional<Ingredient> ingredientOpt = ingredientRepository.findById(ingredientId);
        if (ingredientOpt.isPresent()) {
            Ingredient ingredient = ingredientOpt.get();
            ingredient.setAvailable(isAvailable);
            return ingredientRepository.save(ingredient);
        }
        throw new RuntimeException("Ingredient not found with id: " + ingredientId);
    }

    public Ingredient updateIngredientPrice(Long ingredientId, BigDecimal newPrice) {
        Optional<Ingredient> ingredientOpt = ingredientRepository.findById(ingredientId);
        if (ingredientOpt.isPresent()) {
            Ingredient ingredient = ingredientOpt.get();
            ingredient.setAdditionalPrice(newPrice);
            return ingredientRepository.save(ingredient);
        }
        throw new RuntimeException("Ingredient not found with id: " + ingredientId);
    }

    public void deleteIngredient(Long ingredientId) {
        ingredientRepository.deleteById(ingredientId);
    }

    // === COMBINED MENU METHODS ===

    @Transactional(readOnly = true)
    public List<Pizza> searchMenu(String searchTerm, Pizza.PizzaCategory category, 
                                  BigDecimal maxPrice, Pizza.PizzaSize size, boolean vegetarianOnly) {
        List<Pizza> pizzas = pizzaRepository.findByAvailable(true);
        
        return pizzas.stream()
                .filter(pizza -> searchTerm == null || 
                        pizza.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        pizza.getDescription().toLowerCase().contains(searchTerm.toLowerCase()))
                .filter(pizza -> category == null || pizza.getCategory() == category)
                .filter(pizza -> maxPrice == null || getPizzaPrice(pizza.getId(), size).compareTo(maxPrice) <= 0)
                .filter(pizza -> !vegetarianOnly || pizza.getCategory() == Pizza.PizzaCategory.VEGAN)
                .toList();
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateItemPrice(Long pizzaId, Pizza.PizzaSize size, 
                                        List<Long> additionalIngredients, List<Long> removedIngredients) {
        BigDecimal basePrice = getPizzaPrice(pizzaId, size);
        
        // Add price for additional ingredients
        if (additionalIngredients != null && !additionalIngredients.isEmpty()) {
            for (Long ingredientId : additionalIngredients) {
                Optional<Ingredient> ingredientOpt = ingredientRepository.findById(ingredientId);
                if (ingredientOpt.isPresent()) {
                    basePrice = basePrice.add(ingredientOpt.get().getAdditionalPrice());
                }
            }
        }
        
        // Note: Removed ingredients typically don't reduce price, but could be implemented
        
        return basePrice;
    }

    @Transactional(readOnly = true)
    public boolean validatePizzaCustomization(Long pizzaId, List<Long> additionalIngredients, 
                                             List<Long> removedIngredients) {
        // Check if pizza exists and is available
        Optional<Pizza> pizzaOpt = pizzaRepository.findById(pizzaId);
        if (pizzaOpt.isEmpty() || !pizzaOpt.get().getAvailable()) {
            return false;
        }
        
        // Check if all additional ingredients exist and are available
        if (additionalIngredients != null) {
            for (Long ingredientId : additionalIngredients) {
                Optional<Ingredient> ingredientOpt = ingredientRepository.findById(ingredientId);
                if (ingredientOpt.isEmpty() || !ingredientOpt.get().getAvailable()) {
                    return false;
                }
            }
        }
        
        // Check if all removed ingredients exist (they should be part of the original pizza)
        if (removedIngredients != null) {
            for (Long ingredientId : removedIngredients) {
                if (ingredientRepository.findById(ingredientId).isEmpty()) {
                    return false;
                }
            }
        }
        
        return true;
    }

    @Transactional(readOnly = true)
    public Long countAvailablePizzas() {
        return pizzaRepository.countByAvailable(true);
    }

    @Transactional(readOnly = true)
    public Long countAvailableIngredients() {
        return ingredientRepository.countByAvailable(true);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getMenuStatistics() {
        // This could return statistics about pizzas and ingredients
        return pizzaRepository.findPizzaStatisticsByCategory();
    }
}