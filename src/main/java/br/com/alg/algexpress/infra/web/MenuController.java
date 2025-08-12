package br.com.alg.algexpress.infra.web;

import br.com.alg.algexpress.domain.menu.Ingredient;
import br.com.alg.algexpress.domain.menu.Pizza;
import br.com.alg.algexpress.dto.menu.IngredientRequestDTO;
import br.com.alg.algexpress.dto.menu.IngredientResponseDTO;
import br.com.alg.algexpress.dto.menu.PizzaRequestDTO;
import br.com.alg.algexpress.dto.menu.PizzaResponseDTO;
import br.com.alg.algexpress.infra.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cardapio")
@CrossOrigin(origins = "*")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    // === PIZZA ENDPOINTS ===

    @GetMapping("/pizzas")
    public ResponseEntity<List<PizzaResponseDTO>> getAllPizzas() {
        List<Pizza> pizzas = menuService.findAvailablePizzas();
        List<PizzaResponseDTO> response = pizzas.stream()
                .map(PizzaResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pizzas/{id}")
    public ResponseEntity<PizzaResponseDTO> getPizzaById(@PathVariable Long id) {
        return menuService.findPizzaById(id)
                .map(pizza -> ResponseEntity.ok(PizzaResponseDTO.fromEntity(pizza)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pizzas/category/{category}")
    public ResponseEntity<List<PizzaResponseDTO>> getPizzasByCategory(@PathVariable Pizza.PizzaCategory category) {
        List<Pizza> pizzas = menuService.findPizzasByCategory(category);
        List<PizzaResponseDTO> response = pizzas.stream()
                .map(PizzaResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pizzas/search")
    public ResponseEntity<List<PizzaResponseDTO>> searchPizzas(@RequestParam String name) {
        List<Pizza> pizzas = menuService.findPizzasByNameContaining(name);
        List<PizzaResponseDTO> response = pizzas.stream()
                .map(PizzaResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pizzas/popular")
    public ResponseEntity<List<PizzaResponseDTO>> getMostPopularPizzas() {
        List<Pizza> pizzas = menuService.findMostPopularPizzas();
        List<PizzaResponseDTO> response = pizzas.stream()
                .map(PizzaResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pizzas/vegetarian")
    public ResponseEntity<List<PizzaResponseDTO>> getVegetarianPizzas() {
        List<Pizza> pizzas = menuService.findVegetarianPizzas();
        List<PizzaResponseDTO> response = pizzas.stream()
                .map(PizzaResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pizzas/with-ingredient/{ingredientId}")
    public ResponseEntity<List<PizzaResponseDTO>> getPizzasWithIngredient(@PathVariable Long ingredientId) {
        List<Pizza> pizzas = menuService.findPizzasWithIngredient(ingredientId);
        List<PizzaResponseDTO> response = pizzas.stream()
                .map(PizzaResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pizzas/price-range")
    public ResponseEntity<List<PizzaResponseDTO>> getPizzasByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam Pizza.PizzaSize size) {
        List<Pizza> pizzas = menuService.findPizzasByPriceRange(minPrice, maxPrice, size);
        List<PizzaResponseDTO> response = pizzas.stream()
                .map(PizzaResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pizzas")
    public ResponseEntity<PizzaResponseDTO> createPizza(@Valid @RequestBody PizzaRequestDTO request) {
        Pizza pizza = new Pizza();
        pizza.setName(request.getName());
        pizza.setDescription(request.getDescription());
        pizza.setCategory(request.getCategory());
        pizza.setPriceSmall(request.getPriceSmall());
        pizza.setPriceMedium(request.getPriceMedium());
        pizza.setPriceLarge(request.getPriceLarge());
        pizza.setPriceFamily(request.getPriceFamily());
        pizza.setIsVegetarian(request.getIsVegetarian());
        pizza.setIsAvailable(request.getIsAvailable());

        Pizza savedPizza = menuService.savePizza(pizza);
        return ResponseEntity.status(HttpStatus.CREATED).body(PizzaResponseDTO.fromEntity(savedPizza));
    }

    @PutMapping("/pizzas/{id}")
    public ResponseEntity<PizzaResponseDTO> updatePizza(@PathVariable Long id, @Valid @RequestBody PizzaRequestDTO request) {
        return menuService.findPizzaById(id)
                .map(pizza -> {
                    pizza.setName(request.getName());
                    pizza.setDescription(request.getDescription());
                    pizza.setCategory(request.getCategory());
                    pizza.setPriceSmall(request.getPriceSmall());
                    pizza.setPriceMedium(request.getPriceMedium());
                    pizza.setPriceLarge(request.getPriceLarge());
                    pizza.setPriceFamily(request.getPriceFamily());
                    pizza.setIsVegetarian(request.getIsVegetarian());
                    pizza.setIsAvailable(request.getIsAvailable());

                    Pizza updatedPizza = menuService.savePizza(pizza);
                    return ResponseEntity.ok(PizzaResponseDTO.fromEntity(updatedPizza));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/pizzas/{id}/availability")
    public ResponseEntity<PizzaResponseDTO> updatePizzaAvailability(@PathVariable Long id, @RequestParam boolean isAvailable) {
        try {
            Pizza pizza = menuService.updatePizzaAvailability(id, isAvailable);
            return ResponseEntity.ok(PizzaResponseDTO.fromEntity(pizza));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/pizzas/{id}/price")
    public ResponseEntity<PizzaResponseDTO> updatePizzaPrice(@PathVariable Long id, @RequestParam Pizza.PizzaSize size, @RequestParam BigDecimal newPrice) {
        try {
            Pizza pizza = menuService.updatePizzaPrice(id, size, newPrice);
            return ResponseEntity.ok(PizzaResponseDTO.fromEntity(pizza));
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
    public ResponseEntity<List<IngredientResponseDTO>> getAllIngredients() {
        List<Ingredient> ingredients = menuService.findAvailableIngredients();
        List<IngredientResponseDTO> response = ingredients.stream()
                .map(IngredientResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ingredientes/{id}")
    public ResponseEntity<IngredientResponseDTO> getIngredientById(@PathVariable Long id) {
        return menuService.findIngredientById(id)
                .map(ingredient -> ResponseEntity.ok(IngredientResponseDTO.fromEntity(ingredient)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ingredientes/category/{category}")
    public ResponseEntity<List<IngredientResponseDTO>> getIngredientsByCategory(@PathVariable Ingredient.IngredientCategory category) {
        List<Ingredient> ingredients = menuService.findIngredientsByCategory(category);
        List<IngredientResponseDTO> response = ingredients.stream()
                .map(IngredientResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ingredientes/search")
    public ResponseEntity<List<IngredientResponseDTO>> searchIngredients(@RequestParam String name) {
        List<Ingredient> ingredients = menuService.findIngredientsByNameContaining(name);
        List<IngredientResponseDTO> response = ingredients.stream()
                .map(IngredientResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ingredientes/vegetarian")
    public ResponseEntity<List<IngredientResponseDTO>> getVegetarianIngredients() {
        List<Ingredient> ingredients = menuService.findVegetarianIngredients();
        List<IngredientResponseDTO> response = ingredients.stream()
                .map(IngredientResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ingredientes/most-used")
    public ResponseEntity<List<IngredientResponseDTO>> getMostUsedIngredients() {
        List<Ingredient> ingredients = menuService.findMostUsedIngredients();
        List<IngredientResponseDTO> response = ingredients.stream()
                .map(IngredientResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ingredientes/price-range")
    public ResponseEntity<List<IngredientResponseDTO>> getIngredientsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<Ingredient> ingredients = menuService.findIngredientsByPriceRange(minPrice, maxPrice);
        List<IngredientResponseDTO> response = ingredients.stream()
                .map(IngredientResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/ingredientes")
    public ResponseEntity<IngredientResponseDTO> createIngredient(@Valid @RequestBody IngredientRequestDTO request) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(request.getName());
        ingredient.setDescription(request.getDescription());
        ingredient.setCategory(request.getCategory());
        ingredient.setAdditionalPrice(request.getAdditionalPrice());
        ingredient.setIsVegetarian(request.getIsVegetarian());
        ingredient.setIsAvailable(request.getIsAvailable());

        Ingredient savedIngredient = menuService.saveIngredient(ingredient);
        return ResponseEntity.status(HttpStatus.CREATED).body(IngredientResponseDTO.fromEntity(savedIngredient));
    }

    @PutMapping("/ingredientes/{id}")
    public ResponseEntity<IngredientResponseDTO> updateIngredient(@PathVariable Long id, @Valid @RequestBody IngredientRequestDTO request) {
        return menuService.findIngredientById(id)
                .map(ingredient -> {
                    ingredient.setName(request.getName());
                    ingredient.setDescription(request.getDescription());
                    ingredient.setCategory(request.getCategory());
                    ingredient.setAdditionalPrice(request.getAdditionalPrice());
                    ingredient.setIsVegetarian(request.getIsVegetarian());
                    ingredient.setIsAvailable(request.getIsAvailable());

                    Ingredient updatedIngredient = menuService.saveIngredient(ingredient);
                    return ResponseEntity.ok(IngredientResponseDTO.fromEntity(updatedIngredient));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/ingredientes/{id}/availability")
    public ResponseEntity<IngredientResponseDTO> updateIngredientAvailability(@PathVariable Long id, @RequestParam boolean isAvailable) {
        try {
            Ingredient ingredient = menuService.updateIngredientAvailability(id, isAvailable);
            return ResponseEntity.ok(IngredientResponseDTO.fromEntity(ingredient));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/ingredientes/{id}/price")
    public ResponseEntity<IngredientResponseDTO> updateIngredientPrice(@PathVariable Long id, @RequestParam BigDecimal newPrice) {
        try {
            Ingredient ingredient = menuService.updateIngredientPrice(id, newPrice);
            return ResponseEntity.ok(IngredientResponseDTO.fromEntity(ingredient));
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
    public ResponseEntity<List<PizzaResponseDTO>> searchMenu(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Pizza.PizzaCategory category,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Pizza.PizzaSize size,
            @RequestParam(defaultValue = "false") boolean vegetarianOnly) {
        
        List<Pizza> pizzas = menuService.searchMenu(searchTerm, category, maxPrice, size, vegetarianOnly);
        List<PizzaResponseDTO> response = pizzas.stream()
                .map(PizzaResponseDTO::fromEntity)
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