package br.com.alg.algexpress.infra.web;

import br.com.alg.algexpress.domain.order.Order;
import br.com.alg.algexpress.dto.order.OrderDTO;
import br.com.alg.algexpress.infra.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
@CrossOrigin(origins = "*")
public class OrderControllerSimple {

    private final OrderService orderService;

    public OrderControllerSimple(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<Order> orders = orderService.findActiveOrders();
        return ResponseEntity.ok(orders.stream().map(OrderDTO::fromEntity).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(order -> ResponseEntity.ok(OrderDTO.fromEntity(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomer(@PathVariable Long customerId) {
        List<Order> orders = orderService.findByCustomerId(customerId);
        return ResponseEntity.ok(orders.stream().map(OrderDTO::fromEntity).toList());
    }

    @GetMapping("/today")
    public ResponseEntity<List<OrderDTO>> getTodaysOrders() {
        List<Order> orders = orderService.findTodaysOrders();
        return ResponseEntity.ok(orders.stream().map(OrderDTO::fromEntity).toList());
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
}