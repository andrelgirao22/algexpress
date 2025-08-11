package br.com.alg.algexpress.services;

import br.com.alg.algexpress.domain.menu.Ingredient;
import br.com.alg.algexpress.domain.menu.Pizza;
import br.com.alg.algexpress.domain.order.Order;
import br.com.alg.algexpress.domain.order.OrderItem;
import br.com.alg.algexpress.repository.order.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final MenuService menuService;

    public OrderItemService(OrderItemRepository orderItemRepository, MenuService menuService) {
        this.orderItemRepository = orderItemRepository;
        this.menuService = menuService;
    }

    @Transactional(readOnly = true)
    public Optional<OrderItem> findById(Long id) {
        return orderItemRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<OrderItem> findByOrder(Order order) {
        return orderItemRepository.findByOrder(order);
    }

    @Transactional(readOnly = true)
    public List<OrderItem> findByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    @Transactional(readOnly = true)
    public List<OrderItem> findByPizza(Pizza pizza) {
        return orderItemRepository.findByPizza(pizza);
    }

    @Transactional(readOnly = true)
    public List<OrderItem> findByPizzaId(Long pizzaId) {
        return orderItemRepository.findByPizzaId(pizzaId);
    }

    @Transactional(readOnly = true)
    public List<OrderItem> findBySize(Pizza.PizzaSize size) {
        return orderItemRepository.findBySize(size);
    }

    @Transactional(readOnly = true)
    public List<Object[]> findMostOrderedPizzas() {
        return orderItemRepository.findMostOrderedPizzas();
    }

    @Transactional(readOnly = true)
    public List<Object[]> findMostOrderedSizes() {
        return orderItemRepository.findMostOrderedSizes();
    }

    @Transactional(readOnly = true)
    public List<Object[]> findMostRequestedAdditionalIngredients() {
        return orderItemRepository.findMostRequestedAdditionalIngredients();
    }

    @Transactional(readOnly = true)
    public List<Object[]> findMostRemovedIngredients() {
        return orderItemRepository.findMostRemovedIngredients();
    }

    public OrderItem save(OrderItem orderItem) {
        // Calculate subtotal before saving
        orderItem.setSubtotal(calculateItemSubtotal(orderItem));
        return orderItemRepository.save(orderItem);
    }

    public OrderItem createOrderItem(Long orderId, Long pizzaId, Pizza.PizzaSize size, 
                                    Integer quantity, List<Long> additionalIngredients, 
                                    List<Long> removedIngredients, String notes) {
        
        // Validate the pizza customization
        if (!menuService.validatePizzaCustomization(pizzaId, additionalIngredients, removedIngredients)) {
            throw new RuntimeException("Invalid pizza customization");
        }
        
        // Create new OrderItem
        OrderItem orderItem = new OrderItem();
        
        // Set basic fields - Note: You would need to fetch Order and Pizza entities
        // For now, we'll create a simplified version
        // orderItem.setOrder(orderRepository.findById(orderId).orElseThrow());
        // orderItem.setPizza(pizzaRepository.findById(pizzaId).orElseThrow());
        
        orderItem.setSize(size);
        orderItem.setQuantity(quantity);
        orderItem.setNotes(notes);
        
        // Calculate and set unit price
        BigDecimal unitPrice = menuService.calculateItemPrice(pizzaId, size, additionalIngredients, removedIngredients);
        orderItem.setUnitPrice(unitPrice);
        
        // Calculate and set subtotal
        BigDecimal subtotal = unitPrice.multiply(new BigDecimal(quantity));
        orderItem.setSubtotal(subtotal);
        
        return orderItemRepository.save(orderItem);
    }

    public OrderItem updateQuantity(Long orderItemId, Integer newQuantity) {
        Optional<OrderItem> orderItemOpt = orderItemRepository.findById(orderItemId);
        if (orderItemOpt.isPresent()) {
            OrderItem orderItem = orderItemOpt.get();
            orderItem.setQuantity(newQuantity);
            
            // Recalculate subtotal
            orderItem.setSubtotal(orderItem.getUnitPrice().multiply(new BigDecimal(newQuantity)));
            
            return orderItemRepository.save(orderItem);
        }
        throw new RuntimeException("Order item not found with id: " + orderItemId);
    }

    public OrderItem addAdditionalIngredient(Long orderItemId, Long ingredientId) {
        Optional<OrderItem> orderItemOpt = orderItemRepository.findById(orderItemId);
        if (orderItemOpt.isPresent()) {
            OrderItem orderItem = orderItemOpt.get();
            
            // Check if ingredient exists and is available
            Optional<Ingredient> ingredientOpt = menuService.findIngredientById(ingredientId);
            if (ingredientOpt.isEmpty() || !ingredientOpt.get().getIsAvailable()) {
                throw new RuntimeException("Ingredient not available");
            }
            
            // Add ingredient to additional ingredients list
            orderItem.getAdditionalIngredients().add(ingredientOpt.get());
            
            // Recalculate prices
            recalculateOrderItemPrice(orderItem);
            
            return orderItemRepository.save(orderItem);
        }
        throw new RuntimeException("Order item not found with id: " + orderItemId);
    }

    public OrderItem removeAdditionalIngredient(Long orderItemId, Long ingredientId) {
        Optional<OrderItem> orderItemOpt = orderItemRepository.findById(orderItemId);
        if (orderItemOpt.isPresent()) {
            OrderItem orderItem = orderItemOpt.get();
            
            // Remove ingredient from additional ingredients list
            orderItem.getAdditionalIngredients().removeIf(ingredient -> ingredient.getId().equals(ingredientId));
            
            // Recalculate prices
            recalculateOrderItemPrice(orderItem);
            
            return orderItemRepository.save(orderItem);
        }
        throw new RuntimeException("Order item not found with id: " + orderItemId);
    }

    public OrderItem addRemovedIngredient(Long orderItemId, Long ingredientId) {
        Optional<OrderItem> orderItemOpt = orderItemRepository.findById(orderItemId);
        if (orderItemOpt.isPresent()) {
            OrderItem orderItem = orderItemOpt.get();
            
            // Check if ingredient exists
            Optional<Ingredient> ingredientOpt = menuService.findIngredientById(ingredientId);
            if (ingredientOpt.isEmpty()) {
                throw new RuntimeException("Ingredient not found");
            }
            
            // Add ingredient to removed ingredients list
            orderItem.getRemovedIngredients().add(ingredientOpt.get());
            
            return orderItemRepository.save(orderItem);
        }
        throw new RuntimeException("Order item not found with id: " + orderItemId);
    }

    public OrderItem removeRemovedIngredient(Long orderItemId, Long ingredientId) {
        Optional<OrderItem> orderItemOpt = orderItemRepository.findById(orderItemId);
        if (orderItemOpt.isPresent()) {
            OrderItem orderItem = orderItemOpt.get();
            
            // Remove ingredient from removed ingredients list
            orderItem.getRemovedIngredients().removeIf(ingredient -> ingredient.getId().equals(ingredientId));
            
            return orderItemRepository.save(orderItem);
        }
        throw new RuntimeException("Order item not found with id: " + orderItemId);
    }

    public OrderItem updateNotes(Long orderItemId, String notes) {
        Optional<OrderItem> orderItemOpt = orderItemRepository.findById(orderItemId);
        if (orderItemOpt.isPresent()) {
            OrderItem orderItem = orderItemOpt.get();
            orderItem.setNotes(notes);
            return orderItemRepository.save(orderItem);
        }
        throw new RuntimeException("Order item not found with id: " + orderItemId);
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateItemSubtotal(OrderItem orderItem) {
        BigDecimal basePrice = menuService.getPizzaPrice(orderItem.getPizza().getId(), orderItem.getSize());
        
        // Add additional ingredients price
        if (orderItem.getAdditionalIngredients() != null) {
            for (Ingredient ingredient : orderItem.getAdditionalIngredients()) {
                basePrice = basePrice.add(ingredient.getAdditionalPrice());
            }
        }
        
        // Multiply by quantity
        return basePrice.multiply(new BigDecimal(orderItem.getQuantity()));
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateOrderTotal(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        return orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public String getItemDescription(Long orderItemId) {
        Optional<OrderItem> orderItemOpt = orderItemRepository.findById(orderItemId);
        if (orderItemOpt.isPresent()) {
            OrderItem orderItem = orderItemOpt.get();
            StringBuilder description = new StringBuilder();
            
            description.append(orderItem.getPizza().getName())
                    .append(" (").append(orderItem.getSize().toString()).append(")");
            
            if (orderItem.getQuantity() > 1) {
                description.append(" x").append(orderItem.getQuantity());
            }
            
            if (orderItem.getAdditionalIngredients() != null && !orderItem.getAdditionalIngredients().isEmpty()) {
                description.append(" + ");
                description.append(orderItem.getAdditionalIngredients().stream()
                        .map(Ingredient::getName)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse(""));
            }
            
            if (orderItem.getRemovedIngredients() != null && !orderItem.getRemovedIngredients().isEmpty()) {
                description.append(" - ");
                description.append(orderItem.getRemovedIngredients().stream()
                        .map(Ingredient::getName)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse(""));
            }
            
            if (orderItem.getNotes() != null && !orderItem.getNotes().isEmpty()) {
                description.append(" (").append(orderItem.getNotes()).append(")");
            }
            
            return description.toString();
        }
        throw new RuntimeException("Order item not found with id: " + orderItemId);
    }

    private void recalculateOrderItemPrice(OrderItem orderItem) {
        List<Long> additionalIngredientIds = orderItem.getAdditionalIngredients().stream()
                .map(Ingredient::getId)
                .toList();
        
        List<Long> removedIngredientIds = orderItem.getRemovedIngredients().stream()
                .map(Ingredient::getId)
                .toList();
        
        BigDecimal unitPrice = menuService.calculateItemPrice(
                orderItem.getPizza().getId(), 
                orderItem.getSize(), 
                additionalIngredientIds, 
                removedIngredientIds
        );
        
        orderItem.setUnitPrice(unitPrice);
        orderItem.setSubtotal(unitPrice.multiply(new BigDecimal(orderItem.getQuantity())));
    }

    public void deleteOrderItem(Long orderItemId) {
        orderItemRepository.deleteById(orderItemId);
    }
}