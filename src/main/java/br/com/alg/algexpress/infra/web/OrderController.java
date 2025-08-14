package br.com.alg.algexpress.infra.web;

import br.com.alg.algexpress.domain.valueObjects.Address;
import br.com.alg.algexpress.domain.customer.Customer;
import br.com.alg.algexpress.domain.menu.Pizza;
import br.com.alg.algexpress.domain.order.Order;
import br.com.alg.algexpress.domain.order.OrderItem;
// AddressService removido - addresses são gerenciados via CustomerService
import br.com.alg.algexpress.dto.order.OrderDTO;
import br.com.alg.algexpress.dto.order.OrderSummaryDTO;
import br.com.alg.algexpress.dto.order.OrderItemDTO;
import br.com.alg.algexpress.infra.service.CustomerService;
import br.com.alg.algexpress.infra.service.MenuService;
import br.com.alg.algexpress.infra.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pedidos")
@CrossOrigin(origins = "*")
@Tag(name = "Pedidos", description = "API para gerenciamento de pedidos de pizzas")
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final MenuService menuService;

    public OrderController(OrderService orderService, CustomerService customerService, 
                          MenuService menuService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.menuService = menuService;
    }

    @GetMapping
    public ResponseEntity<List<OrderSummaryDTO>> getAllOrders() {
        List<Order> orders = orderService.findActiveOrders();
        List<OrderSummaryDTO> response = orders.stream()
                .map(OrderSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(order -> ResponseEntity.ok(OrderDTO.fromEntity(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderSummaryDTO>> getOrdersByCustomer(@PathVariable Long customerId) {
        List<Order> orders = orderService.findByCustomerId(customerId);
        List<OrderSummaryDTO> response = orders.stream()
                .map(OrderSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}/history")
    public ResponseEntity<List<OrderSummaryDTO>> getCustomerOrderHistory(@PathVariable Long customerId) {
        List<Order> orders = orderService.findCustomerOrderHistory(customerId);
        List<OrderSummaryDTO> response = orders.stream()
                .map(OrderSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderSummaryDTO>> getOrdersByStatus(@PathVariable Order.OrderStatus status) {
        List<Order> orders = orderService.findByStatus(status);
        List<OrderSummaryDTO> response = orders.stream()
                .map(OrderSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<OrderSummaryDTO>> getOrdersByType(@PathVariable Order.OrderType type) {
        List<Order> orders = orderService.findByType(type);
        List<OrderSummaryDTO> response = orders.stream()
                .map(OrderSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/today")
    public ResponseEntity<List<OrderSummaryDTO>> getTodaysOrders() {
        List<Order> orders = orderService.findTodaysOrders();
        List<OrderSummaryDTO> response = orders.stream()
                .map(OrderSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<OrderSummaryDTO>> getActiveOrders() {
        List<Order> orders = orderService.findActiveOrders();
        List<OrderSummaryDTO> response = orders.stream()
                .map(OrderSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/delivery/active")
    public ResponseEntity<List<OrderSummaryDTO>> getActiveDeliveryOrders() {
        List<Order> orders = orderService.findActiveDeliveryOrders();
        List<OrderSummaryDTO> response = orders.stream()
                .map(OrderSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<OrderSummaryDTO>> getOrdersBetweenDates(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<Order> orders = orderService.findOrdersBetweenDates(startDate, endDate);
        List<OrderSummaryDTO> response = orders.stream()
                .map(OrderSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/total-range")
    public ResponseEntity<List<OrderSummaryDTO>> getOrdersByTotalRange(
            @RequestParam BigDecimal minTotal,
            @RequestParam BigDecimal maxTotal) {
        List<Order> orders = orderService.findByTotalBetween(minTotal, maxTotal);
        List<OrderSummaryDTO> response = orders.stream()
                .map(OrderSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}/status/{status}")
    public ResponseEntity<List<OrderSummaryDTO>> getOrdersByCustomerAndStatus(
            @PathVariable Long customerId,
            @PathVariable Order.OrderStatus status) {
        List<Order> orders = orderService.findByCustomerIdAndStatus(customerId, status);
        List<OrderSummaryDTO> response = orders.stream()
                .map(OrderSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO request) {
        try {
            Customer customer = customerService.findById(request.customerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            Order order = new Order();
            order.setCustomer(customer);
            order.setType(request.type());
            order.setObservations(request.observations());

            if (request.type() == Order.OrderType.DELIVERY) {
                if (request.addressId() == null) {
                    throw new RuntimeException("Address is required for delivery orders");
                }
                
                Address address = customerService.findAddressById(request.addressId())
                        .orElseThrow(() -> new RuntimeException("Address not found"));
                
                // TODO: Implement delivery area validation in CustomerService
                // if (!customerService.isAddressInDeliveryArea(request.addressId())) {
                //     throw new RuntimeException("Address is not in delivery area");
                // }
                
                order.setDeliveryAddress(address);
                // TODO: Implement delivery fee calculation in CustomerService
                // order.setDeliveryFee(customerService.calculateDeliveryFee(request.addressId()));
            }

            List<OrderItem> orderItems = new ArrayList<>();
            for (OrderItemDTO itemRequest : request.items()) {
                Pizza pizza = menuService.findPizzaById(itemRequest.pizzaId())
                        .orElseThrow(() -> new RuntimeException("Pizza not found"));

                if (!pizza.getAvailable()) {
                    throw new RuntimeException("Pizza is not available: " + pizza.getName());
                }

                if (!menuService.validatePizzaCustomization(itemRequest.pizzaId(), 
                        itemRequest.additionalIngredientIds(), itemRequest.removedIngredientIds())) {
                    throw new RuntimeException("Invalid pizza customization");
                }

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setPizza(pizza);
                orderItem.setSize(itemRequest.size());
                orderItem.setQuantity(itemRequest.quantity());
                orderItem.setObservations(itemRequest.observations());

                BigDecimal unitPrice = menuService.calculateItemPrice(
                    itemRequest.pizzaId(),
                    itemRequest.size(),
                    itemRequest.additionalIngredientIds(),
                    itemRequest.removedIngredientIds()
                );
                orderItem.setUnitPrice(unitPrice);
                orderItem.setTotalPrice(unitPrice.multiply(new BigDecimal(itemRequest.quantity())));

                orderItems.add(orderItem);
            }

            order.setItems(orderItems);
            
            Order savedOrder = orderService.save(order);
            orderService.calculateOrderTotal(savedOrder);
            Order finalOrder = orderService.save(savedOrder);

            return ResponseEntity.status(HttpStatus.CREATED).body(OrderDTO.fromEntity(finalOrder));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long id, @RequestParam Order.OrderStatus status) {
        try {
            Order order = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(OrderDTO.fromEntity(order));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<OrderDTO> confirmOrder(@PathVariable Long id) {
        try {
            Order order = orderService.confirmOrder(id);
            return ResponseEntity.ok(OrderDTO.fromEntity(order));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long id) {
        try {
            Order order = orderService.cancelOrder(id);
            return ResponseEntity.ok(OrderDTO.fromEntity(order));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/ready")
    public ResponseEntity<OrderDTO> markOrderReady(@PathVariable Long id) {
        try {
            Order order = orderService.markAsReady(id);
            return ResponseEntity.ok(OrderDTO.fromEntity(order));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/delivered")
    public ResponseEntity<OrderDTO> markOrderDelivered(@PathVariable Long id) {
        try {
            Order order = orderService.markAsDelivered(id);
            return ResponseEntity.ok(OrderDTO.fromEntity(order));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/calculate-total")
    public ResponseEntity<BigDecimal> calculateOrderTotal(@PathVariable Long id) {
        return orderService.findById(id)
                .map(order -> {
                    BigDecimal total = orderService.calculateOrderTotal(order);
                    return ResponseEntity.ok(total);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // === STATISTICS ===

    @GetMapping("/statistics/count-by-status")
    public ResponseEntity<Long> getCountByStatus(@RequestParam Order.OrderStatus status) {
        Long count = orderService.countByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/statistics/count-by-date-range")
    public ResponseEntity<Long> getCountByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        Long count = orderService.countOrdersBetweenDates(startDate, endDate);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/statistics/revenue-by-date-range")
    public ResponseEntity<BigDecimal> getRevenueByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        return orderService.getTotalRevenueBetweenDates(startDate, endDate)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(BigDecimal.ZERO));
    }

    @GetMapping("/statistics/average-order-total")
    public ResponseEntity<BigDecimal> getAverageOrderTotal() {
        return orderService.getAverageOrderTotal()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(BigDecimal.ZERO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (orderService.findById(id).isPresent()) {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}