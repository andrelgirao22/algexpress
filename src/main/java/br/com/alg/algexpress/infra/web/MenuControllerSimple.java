package br.com.alg.algexpress.infra.web;

import br.com.alg.algexpress.domain.menu.Ingredient;
import br.com.alg.algexpress.domain.menu.Pizza;
import br.com.alg.algexpress.dto.menu.IngredientDTO;
import br.com.alg.algexpress.dto.menu.PizzaDTO;
import br.com.alg.algexpress.infra.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cardapio")
@CrossOrigin(origins = "*")
public class MenuControllerSimple {

    private final MenuService menuService;

    public MenuControllerSimple(MenuService menuService) {
        this.menuService = menuService;
    }

    // PIZZA ENDPOINTS
    @GetMapping("/pizzas")
    public ResponseEntity<List<PizzaDTO>> getAllPizzas() {
        List<Pizza> pizzas = menuService.findAvailablePizzas();
        return ResponseEntity.ok(pizzas.stream().map(PizzaDTO::fromEntity).toList());
    }

    @GetMapping("/pizzas/{id}")
    public ResponseEntity<PizzaDTO> getPizzaById(@PathVariable Long id) {
        return menuService.findPizzaById(id)
                .map(pizza -> ResponseEntity.ok(PizzaDTO.fromEntity(pizza)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/pizzas")
    public ResponseEntity<PizzaDTO> createPizza(@Valid @RequestBody PizzaDTO dto) {
        Pizza pizza = dto.toEntity();
        Pizza saved = menuService.savePizza(pizza);
        return ResponseEntity.status(HttpStatus.CREATED).body(PizzaDTO.fromEntity(saved));
    }

    @PutMapping("/pizzas/{id}")
    public ResponseEntity<PizzaDTO> updatePizza(@PathVariable Long id, @Valid @RequestBody PizzaDTO dto) {
        return menuService.findPizzaById(id)
                .map(existing -> {
                    existing.setName(dto.name());
                    existing.setDescription(dto.description());
                    existing.setCategory(dto.category());
                    existing.setPriceSmall(dto.priceSmall());
                    existing.setPriceMedium(dto.priceMedium());
                    existing.setPriceLarge(dto.priceLarge());
                    existing.setPriceFamily(dto.priceFamily());
                    existing.setIsVegetarian(dto.isVegetarian());
                    existing.setIsAvailable(dto.isAvailable());
                    Pizza updated = menuService.savePizza(existing);
                    return ResponseEntity.ok(PizzaDTO.fromEntity(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // INGREDIENT ENDPOINTS
    @GetMapping("/ingredientes")
    public ResponseEntity<List<IngredientDTO>> getAllIngredients() {
        List<Ingredient> ingredients = menuService.findAvailableIngredients();
        return ResponseEntity.ok(ingredients.stream().map(IngredientDTO::fromEntity).toList());
    }

    @GetMapping("/ingredientes/{id}")
    public ResponseEntity<IngredientDTO> getIngredientById(@PathVariable Long id) {
        return menuService.findIngredientById(id)
                .map(ingredient -> ResponseEntity.ok(IngredientDTO.fromEntity(ingredient)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/ingredientes")
    public ResponseEntity<IngredientDTO> createIngredient(@Valid @RequestBody IngredientDTO dto) {
        Ingredient ingredient = dto.toEntity();
        Ingredient saved = menuService.saveIngredient(ingredient);
        return ResponseEntity.status(HttpStatus.CREATED).body(IngredientDTO.fromEntity(saved));
    }
}