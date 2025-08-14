package br.com.alg.algexpress.infra.web;

import br.com.alg.algexpress.domain.menu.Ingredient;
import br.com.alg.algexpress.domain.menu.Pizza;
import br.com.alg.algexpress.dto.menu.IngredientDTO;
import br.com.alg.algexpress.dto.menu.PizzaDTO;
import br.com.alg.algexpress.dto.menu.PizzaSummaryDTO;
import br.com.alg.algexpress.infra.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cardapio")
@CrossOrigin(origins = "*")
@Tag(name = "Cardápio", description = "API para gerenciamento de pizzas e ingredientes")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    // === PIZZA ENDPOINTS ===

    @GetMapping("/pizzas")
    public ResponseEntity<List<PizzaSummaryDTO>> getAllPizzas() {
        List<Pizza> pizzas = menuService.findAvailablePizzas();
        List<PizzaSummaryDTO> response = pizzas.stream()
                .map(PizzaSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pizzas/{id}")
    public ResponseEntity<PizzaDTO> getPizzaById(@PathVariable Long id) {
        return menuService.findPizzaById(id)
                .map(pizza -> ResponseEntity.ok(PizzaDTO.fromEntity(pizza)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pizzas/category/{category}")
    public ResponseEntity<List<PizzaSummaryDTO>> getPizzasByCategory(@PathVariable Pizza.PizzaCategory category) {
        List<Pizza> pizzas = menuService.findPizzasByCategory(category);
        List<PizzaSummaryDTO> response = pizzas.stream()
                .map(PizzaSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pizzas/search")
    public ResponseEntity<List<PizzaSummaryDTO>> searchPizzas(@RequestParam String name) {
        List<Pizza> pizzas = menuService.findPizzasByNameContaining(name);
        List<PizzaSummaryDTO> response = pizzas.stream()
                .map(PizzaSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pizzas/popular")
    public ResponseEntity<List<PizzaSummaryDTO>> getMostPopularPizzas() {
        List<Pizza> pizzas = menuService.findMostPopularPizzas();
        List<PizzaSummaryDTO> response = pizzas.stream()
                .map(PizzaSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pizzas/vegetarian")
    public ResponseEntity<List<PizzaSummaryDTO>> getVegetarianPizzas() {
        List<Pizza> pizzas = menuService.findVegetarianPizzas();
        List<PizzaSummaryDTO> response = pizzas.stream()
                .map(PizzaSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pizzas/with-ingredient/{ingredientId}")
    public ResponseEntity<List<PizzaSummaryDTO>> getPizzasWithIngredient(@PathVariable Long ingredientId) {
        List<Pizza> pizzas = menuService.findPizzasWithIngredient(ingredientId);
        List<PizzaSummaryDTO> response = pizzas.stream()
                .map(PizzaSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pizzas/price-range")
    public ResponseEntity<List<PizzaSummaryDTO>> getPizzasByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam Pizza.PizzaSize size) {
        List<Pizza> pizzas = menuService.findPizzasByPriceRange(minPrice, maxPrice, size);
        List<PizzaSummaryDTO> response = pizzas.stream()
                .map(PizzaSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pizzas")
    public ResponseEntity<PizzaDTO> createPizza(@Valid @RequestBody PizzaDTO request) {
        Pizza pizza = new Pizza();
        pizza.setName(request.name());
        pizza.setDescription(request.description());
        pizza.setCategory(request.category());
        pizza.setPriceSmall(request.priceSmall());
        pizza.setPriceMedium(request.priceMedium());
        pizza.setPriceLarge(request.priceLarge());
        pizza.setPriceExtraLarge(request.priceExtraLarge());
        //pizza.setIsVegetarian(request.getIsVegetarian());
        pizza.setAvailable(request.available());

        Pizza savedPizza = menuService.savePizza(pizza);
        return ResponseEntity.status(HttpStatus.CREATED).body(PizzaDTO.fromEntity(savedPizza));
    }

    @PutMapping("/pizzas/{id}")
    public ResponseEntity<PizzaDTO> updatePizza(@PathVariable Long id, @Valid @RequestBody PizzaDTO request) {
        return menuService.findPizzaById(id)
                .map(pizza -> {
                    pizza.setName(request.name());
                    pizza.setDescription(request.description());
                    pizza.setCategory(request.category());
                    pizza.setPriceSmall(request.priceSmall());
                    pizza.setPriceMedium(request.priceMedium());
                    pizza.setPriceLarge(request.priceLarge());
                    pizza.setPriceExtraLarge(request.priceExtraLarge());
                    //pizza.setIsVegetarian(request.a);
                    pizza.setAvailable(request.available());

                    Pizza updatedPizza = menuService.savePizza(pizza);
                    return ResponseEntity.ok(PizzaDTO.fromEntity(updatedPizza));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/pizzas/{id}/availability")
    public ResponseEntity<PizzaDTO> updatePizzaAvailability(@PathVariable Long id, @RequestParam boolean isAvailable) {
        try {
            Pizza pizza = menuService.updatePizzaAvailability(id, isAvailable);
            return ResponseEntity.ok(PizzaDTO.fromEntity(pizza));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/pizzas/{id}/price")
    public ResponseEntity<PizzaDTO> updatePizzaPrice(@PathVariable Long id, @RequestParam Pizza.PizzaSize size, @RequestParam BigDecimal newPrice) {
        try {
            Pizza pizza = menuService.updatePizzaPrice(id, size, newPrice);
            return ResponseEntity.ok(PizzaDTO.fromEntity(pizza));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/pizzas/{id}/price/{size}")
    public ResponseEntity<BigDecimal> getPizzaPrice(@PathVariable Long id, @PathVariable Pizza.PizzaSize size) {
        try {
            BigDecimal price = menuService.getPizzaPrice(id, size);
            return ResponseEntity.ok(price);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/pizzas/{id}")
    public ResponseEntity<Void> deletePizza(@PathVariable Long id) {
        if (menuService.findPizzaById(id).isPresent()) {
            menuService.deletePizza(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // === INGREDIENT ENDPOINTS ===

    @GetMapping("/ingredientes")
    public ResponseEntity<List<IngredientDTO>> getAllIngredients() {
        List<Ingredient> ingredients = menuService.findAvailableIngredients();
        List<IngredientDTO> response = ingredients.stream()
                .map(IngredientDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ingredientes/{id}")
    public ResponseEntity<IngredientDTO> getIngredientById(@PathVariable Long id) {
        return menuService.findIngredientById(id)
                .map(ingredient -> ResponseEntity.ok(IngredientDTO.fromEntity(ingredient)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ingredientes/category/{category}")
    public ResponseEntity<List<IngredientDTO>> getIngredientsByCategory(@PathVariable Ingredient.IngredientCategory category) {
        List<Ingredient> ingredients = menuService.findIngredientsByCategory(category);
        List<IngredientDTO> response = ingredients.stream()
                .map(IngredientDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ingredientes/search")
    public ResponseEntity<List<IngredientDTO>> searchIngredients(@RequestParam String name) {
        List<Ingredient> ingredients = menuService.findIngredientsByNameContaining(name);
        List<IngredientDTO> response = ingredients.stream()
                .map(IngredientDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ingredientes/vegetarian")
    public ResponseEntity<List<IngredientDTO>> getVegetarianIngredients() {
        List<Ingredient> ingredients = menuService.findVegetarianIngredients();
        List<IngredientDTO> response = ingredients.stream()
                .map(IngredientDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ingredientes/most-used")
    public ResponseEntity<List<IngredientDTO>> getMostUsedIngredients() {
        List<Ingredient> ingredients = menuService.findMostUsedIngredients();
        List<IngredientDTO> response = ingredients.stream()
                .map(IngredientDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ingredientes/price-range")
    public ResponseEntity<List<IngredientDTO>> getIngredientsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<Ingredient> ingredients = menuService.findIngredientsByPriceRange(minPrice, maxPrice);
        List<IngredientDTO> response = ingredients.stream()
                .map(IngredientDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/ingredientes")
    public ResponseEntity<IngredientDTO> createIngredient(@Valid @RequestBody IngredientDTO request) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(request.name());
        ingredient.setDescription(request.description());
        ingredient.setCategory(request.category());
        ingredient.setAdditionalPrice(request.additionalPrice());
        ingredient.setAllergenic(request.allergenic());
        ingredient.setAvailable(request.available());

        Ingredient savedIngredient = menuService.saveIngredient(ingredient);
        return ResponseEntity.status(HttpStatus.CREATED).body(IngredientDTO.fromEntity(savedIngredient));
    }

    @PutMapping("/ingredientes/{id}")
    public ResponseEntity<IngredientDTO> updateIngredient(@PathVariable Long id, @Valid @RequestBody IngredientDTO request) {
        return menuService.findIngredientById(id)
                .map(ingredient -> {
                    ingredient.setName(request.name());
                    ingredient.setDescription(request.description());
                    ingredient.setCategory(request.category());
                    ingredient.setAdditionalPrice(request.additionalPrice());
                    ingredient.setAllergenic(request.allergenic());
                    ingredient.setAvailable(request.available());

                    Ingredient updatedIngredient = menuService.saveIngredient(ingredient);
                    return ResponseEntity.ok(IngredientDTO.fromEntity(updatedIngredient));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/ingredientes/{id}/availability")
    public ResponseEntity<IngredientDTO> updateIngredientAvailability(@PathVariable Long id, @RequestParam boolean isAvailable) {
        try {
            Ingredient ingredient = menuService.updateIngredientAvailability(id, isAvailable);
            return ResponseEntity.ok(IngredientDTO.fromEntity(ingredient));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/ingredientes/{id}/price")
    public ResponseEntity<IngredientDTO> updateIngredientPrice(@PathVariable Long id, @RequestParam BigDecimal newPrice) {
        try {
            Ingredient ingredient = menuService.updateIngredientPrice(id, newPrice);
            return ResponseEntity.ok(IngredientDTO.fromEntity(ingredient));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/ingredientes/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        if (menuService.findIngredientById(id).isPresent()) {
            menuService.deleteIngredient(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // === MENU SEARCH AND UTILITIES ===

    @GetMapping("/search")
    public ResponseEntity<List<PizzaSummaryDTO>> searchMenu(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Pizza.PizzaCategory category,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Pizza.PizzaSize size,
            @RequestParam(defaultValue = "false") boolean vegetarianOnly) {
        
        List<Pizza> pizzas = menuService.searchMenu(searchTerm, category, maxPrice, size, vegetarianOnly);
        List<PizzaSummaryDTO> response = pizzas.stream()
                .map(PizzaSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/calculate-item-price")
    public ResponseEntity<BigDecimal> calculateItemPrice(
            @RequestParam Long pizzaId,
            @RequestParam Pizza.PizzaSize size,
            @RequestParam(required = false) List<Long> additionalIngredients,
            @RequestParam(required = false) List<Long> removedIngredients) {
        
        BigDecimal price = menuService.calculateItemPrice(pizzaId, size, additionalIngredients, removedIngredients);
        return ResponseEntity.ok(price);
    }

    @PostMapping("/validate-customization")
    public ResponseEntity<Boolean> validatePizzaCustomization(
            @RequestParam Long pizzaId,
            @RequestParam(required = false) List<Long> additionalIngredients,
            @RequestParam(required = false) List<Long> removedIngredients) {
        
        boolean isValid = menuService.validatePizzaCustomization(pizzaId, additionalIngredients, removedIngredients);
        return ResponseEntity.ok(isValid);
    }

    // === STATISTICS ===

    @GetMapping("/statistics/pizzas-count")
    public ResponseEntity<Long> getAvailablePizzasCount() {
        Long count = menuService.countAvailablePizzas();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/statistics/ingredients-count")
    public ResponseEntity<Long> getAvailableIngredientsCount() {
        Long count = menuService.countAvailableIngredients();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/statistics/menu-stats")
    public ResponseEntity<List<Object[]>> getMenuStatistics() {
        List<Object[]> statistics = menuService.getMenuStatistics();
        return ResponseEntity.ok(statistics);
    }
}