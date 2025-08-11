package br.com.alg.algexpress.repository.order;

import br.com.alg.algexpress.domain.menu.Pizza;
import br.com.alg.algexpress.domain.order.Order;
import br.com.alg.algexpress.domain.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    List<OrderItem> findByOrder(Order order);
    
    List<OrderItem> findByOrderId(Long orderId);
    
    List<OrderItem> findByPizza(Pizza pizza);
    
    List<OrderItem> findBySize(Pizza.PizzaSize size);
    
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId")
    List<OrderItem> findItemsByOrderId(@Param("orderId") Long orderId);
    
    @Query("SELECT oi FROM OrderItem oi WHERE oi.pizza.id = :pizzaId AND oi.size = :size")
    List<OrderItem> findByPizzaIdAndSize(@Param("pizzaId") Long pizzaId, @Param("size") Pizza.PizzaSize size);
    
    @Query("SELECT SUM(oi.totalPrice) FROM OrderItem oi WHERE oi.order.id = :orderId")
    Optional<BigDecimal> calculateOrderSubtotal(@Param("orderId") Long orderId);
    
    @Query("SELECT oi.pizza, SUM(oi.quantity) as totalSold FROM OrderItem oi " +
           "JOIN oi.order o WHERE o.status = 'DELIVERED' AND o.orderDateTime BETWEEN :startDate AND :endDate " +
           "GROUP BY oi.pizza ORDER BY totalSold DESC")
    List<Object[]> findMostSoldPizzasByDateRange(@Param("startDate") LocalDateTime startDate, 
                                                 @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT oi.size, COUNT(oi) as sizeCount FROM OrderItem oi " +
           "JOIN oi.order o WHERE o.status = 'DELIVERED' " +
           "GROUP BY oi.size ORDER BY sizeCount DESC")
    List<Object[]> findMostPopularSizes();
    
    @Query("SELECT COUNT(oi) FROM OrderItem oi WHERE oi.pizza.id = :pizzaId")
    Long countByPizzaId(@Param("pizzaId") Long pizzaId);
    
    @Query("SELECT AVG(oi.totalPrice) FROM OrderItem oi")
    Optional<BigDecimal> findAverageItemPrice();
    
    @Query("SELECT oi FROM OrderItem oi JOIN oi.additionalIngredients ai WHERE ai.id = :ingredientId")
    List<OrderItem> findItemsWithAdditionalIngredient(@Param("ingredientId") Long ingredientId);
    
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi JOIN oi.order o " +
           "WHERE oi.pizza.id = :pizzaId AND o.status = 'DELIVERED' AND o.orderDateTime BETWEEN :startDate AND :endDate")
    Optional<Long> countPizzaSoldInPeriod(@Param("pizzaId") Long pizzaId, 
                                         @Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
}