package br.com.alg.algexpress.infra.web;

import br.com.alg.algexpress.domain.valueObjects.Address;
import br.com.alg.algexpress.domain.customer.Customer;
import br.com.alg.algexpress.domain.menu.Pizza;
import br.com.alg.algexpress.domain.order.Order;
import br.com.alg.algexpress.domain.order.OrderItem;
import br.com.alg.algexpress.dto.order.OrderItemRequestDTO;
import br.com.alg.algexpress.dto.order.OrderRequestDTO;
import br.com.alg.algexpress.dto.order.OrderResponseDTO;
// AddressService removido - addresses são gerenciados via CustomerService
import br.com.alg.algexpress.infra.service.CustomerService;
import br.com.alg.algexpress.infra.service.MenuService;
import br.com.alg.algexpress.infra.service.OrderService;
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
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<Order> orders = orderService.findActiveOrders();
        List<OrderResponseDTO> response = orders.stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(order -> ResponseEntity.ok(OrderResponseDTO.fromEntity(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByCustomer(@PathVariable Long customerId) {
        List<Order> orders = orderService.findByCustomerId(customerId);
        List<OrderResponseDTO> response = orders.stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}/history")
    public ResponseEntity<List<OrderResponseDTO>> getCustomerOrderHistory(@PathVariable Long customerId) {
        List<Order> orders = orderService.findCustomerOrderHistory(customerId);
        List<OrderResponseDTO> response = orders.stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByStatus(@PathVariable Order.OrderStatus status) {
        List<Order> orders = orderService.findByStatus(status);
        List<OrderResponseDTO> response = orders.stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByType(@PathVariable Order.OrderType type) {
        List<Order> orders = orderService.findByType(type);
        List<OrderResponseDTO> response = orders.stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/today")
    public ResponseEntity<List<OrderResponseDTO>> getTodaysOrders() {
        List<Order> orders = orderService.findTodaysOrders();
        List<OrderResponseDTO> response = orders.stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<OrderResponseDTO>> getActiveOrders() {
        List<Order> orders = orderService.findActiveOrders();
        List<OrderResponseDTO> response = orders.stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/delivery/active")
    public ResponseEntity<List<OrderResponseDTO>> getActiveDeliveryOrders() {
        List<Order> orders = orderService.findActiveDeliveryOrders();
        List<OrderResponseDTO> response = orders.stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersBetweenDates(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<Order> orders = orderService.findOrdersBetweenDates(startDate, endDate);
        List<OrderResponseDTO> response = orders.stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/total-range")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByTotalRange(
            @RequestParam BigDecimal minTotal,
            @RequestParam BigDecimal maxTotal) {
        List<Order> orders = orderService.findByTotalBetween(minTotal, maxTotal);
        List<OrderResponseDTO> response = orders.stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}/status/{status}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByCustomerAndStatus(
            @PathVariable Long customerId,
            @PathVariable Order.OrderStatus status) {
        List<Order> orders = orderService.findByCustomerIdAndStatus(customerId, status);
        List<OrderResponseDTO> response = orders.stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO request) {
        try {
            Customer customer = customerService.findById(request.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            Order order = new Order();
            order.setCustomer(customer);
            order.setType(request.getType());
            order.setObservations(request.getObservations());

            if (request.getType() == Order.OrderType.DELIVERY) {
                if (request.getAddressId() == null) {
                    throw new RuntimeException("Address is required for delivery orders");
                }
                
                Address address = addressService.findById(request.getAddressId())
                        .orElseThrow(() -> new RuntimeException("Address not found"));
                
                if (!addressService.isAddressInDeliveryArea(request.getAddressId())) {
                    throw new RuntimeException("Address is not in delivery area");
                }
                
                order.setAddress(address);
                order.setDeliveryFee(addressService.calculateDeliveryFee(request.getAddressId()));
            }

            List<OrderItem> orderItems = new ArrayList<>();
            for (OrderItemRequestDTO itemRequest : request.getItems()) {
                Pizza pizza = menuService.findPizzaById(itemRequest.getPizzaId())
                        .orElseThrow(() -> new RuntimeException("Pizza not found"));

                if (!pizza.getIsAvailable()) {
                    throw new RuntimeException("Pizza is not available: " + pizza.getName());
                }

                if (!menuService.validatePizzaCustomization(itemRequest.getPizzaId(), 
                        itemRequest.getAdditionalIngredients(), itemRequest.getRemovedIngredients())) {
                    throw new RuntimeException("Invalid pizza customization");
                }

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setPizza(pizza);
                orderItem.setSize(OrderItem.PizzaSize.valueOf(itemRequest.getSize().name()));
                orderItem.setQuantity(itemRequest.getQuantity());
                orderItem.setObservations(itemRequest.getObservations());

                BigDecimal unitPrice = menuService.calculateItemPrice(
                    itemRequest.getPizzaId(),
                    itemRequest.getSize(),
                    itemRequest.getAdditionalIngredients(),
                    itemRequest.getRemovedIngredients()
                );
                orderItem.setUnitPrice(unitPrice);
                orderItem.setSubtotal(unitPrice.multiply(new BigDecimal(itemRequest.getQuantity())));

                orderItems.add(orderItem);
            }

            order.setOrderItems(orderItems);
            
            Order savedOrder = orderService.save(order);
            orderService.calculateOrderTotal(savedOrder);
            Order finalOrder = orderService.save(savedOrder);

            return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponseDTO.fromEntity(finalOrder));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Long id, @RequestParam Order.OrderStatus status) {
        try {
            Order order = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(OrderResponseDTO.fromEntity(order));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<OrderResponseDTO> confirmOrder(@PathVariable Long id) {
        try {
            Order order = orderService.confirmOrder(id);
            return ResponseEntity.ok(OrderResponseDTO.fromEntity(order));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable Long id) {
        try {
            Order order = orderService.cancelOrder(id);
            return ResponseEntity.ok(OrderResponseDTO.fromEntity(order));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/ready")
    public ResponseEntity<OrderResponseDTO> markOrderReady(@PathVariable Long id) {
        try {
            Order order = orderService.markAsReady(id);
            return ResponseEntity.ok(OrderResponseDTO.fromEntity(order));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/delivered")
    public ResponseEntity<OrderResponseDTO> markOrderDelivered(@PathVariable Long id) {
        try {
            Order order = orderService.markAsDelivered(id);
            return ResponseEntity.ok(OrderResponseDTO.fromEntity(order));
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