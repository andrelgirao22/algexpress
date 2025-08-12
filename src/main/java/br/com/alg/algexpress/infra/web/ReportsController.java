package br.com.alg.algexpress.infra.web;

import br.com.alg.algexpress.infra.service.CustomerService;
import br.com.alg.algexpress.infra.service.MenuService;
import br.com.alg.algexpress.infra.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/relatorios")
@CrossOrigin(origins = "*")
public class ReportsController {

    private final CustomerService customerService;
    private final OrderService orderService;
    private final MenuService menuService;

    public ReportsController(CustomerService customerService, OrderService orderService, MenuService menuService) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.menuService = menuService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("totalActiveCustomers", customerService.getTotalActiveCustomers());
        dashboard.put("averageLoyaltyPoints", customerService.getAverageLoyaltyPoints().orElse(BigDecimal.ZERO));
        dashboard.put("averageOrderTotal", orderService.getAverageOrderTotal().orElse(BigDecimal.ZERO));
        dashboard.put("availablePizzas", menuService.countAvailablePizzas());
        dashboard.put("availableIngredients", menuService.countAvailableIngredients());
        
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/revenue")
    public ResponseEntity<BigDecimal> getRevenue(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(
            orderService.getTotalRevenueBetweenDates(startDate, endDate).orElse(BigDecimal.ZERO)
        );
    }

    @GetMapping("/customers/most-orders")
    public ResponseEntity<List<Object[]>> getCustomersWithMostOrders() {
        return ResponseEntity.ok(customerService.findCustomersWithMostOrders().stream().map(c -> new Object[]{c.getId(), c.getName()}).toList());
    }

    @GetMapping("/pizzas/most-popular")
    public ResponseEntity<List<Object[]>> getMostPopularPizzas() {
        return ResponseEntity.ok(menuService.findMostPopularPizzas().stream().map(p -> new Object[]{p.getId(), p.getName()}).toList());
    }

    @GetMapping("/menu/statistics")
    public ResponseEntity<List<Object[]>> getMenuStatistics() {
        return ResponseEntity.ok(menuService.getMenuStatistics());
    }
}