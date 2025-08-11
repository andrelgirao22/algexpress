package br.com.alg.algexpress.repository.order;

import br.com.alg.algexpress.domain.customer.Customer;
import br.com.alg.algexpress.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByCustomer(Customer customer);
    
    List<Order> findByCustomerId(Long customerId);
    
    List<Order> findByStatus(Order.OrderStatus status);
    
    List<Order> findByType(Order.OrderType type);
    
    @Query("SELECT o FROM Order o WHERE o.status = :status ORDER BY o.orderDateTime ASC")
    List<Order> findByStatusOrderByDateTime(@Param("status") Order.OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.orderDateTime BETWEEN :startDate AND :endDate")
    List<Order> findOrdersBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId AND o.status = :status")
    List<Order> findByCustomerIdAndStatus(@Param("customerId") Long customerId, 
                                          @Param("status") Order.OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.total BETWEEN :minTotal AND :maxTotal")
    List<Order> findByTotalBetween(@Param("minTotal") BigDecimal minTotal, 
                                   @Param("maxTotal") BigDecimal maxTotal);
    
    @Query("SELECT o FROM Order o WHERE o.status IN ('CONFIRMED', 'PREPARING', 'READY') ORDER BY o.orderDateTime ASC")
    List<Order> findActiveOrders();
    
    @Query("SELECT o FROM Order o WHERE o.type = 'DELIVERY' AND o.status IN ('CONFIRMED', 'PREPARING', 'READY', 'OUT_FOR_DELIVERY')")
    List<Order> findActiveDeliveryOrders();
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    Long countByStatus(@Param("status") Order.OrderStatus status);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDateTime BETWEEN :startDate AND :endDate")
    Long countOrdersBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                 @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(o.total) FROM Order o WHERE o.status = 'DELIVERED' AND o.orderDateTime BETWEEN :startDate AND :endDate")
    Optional<BigDecimal> sumTotalByDateRange(@Param("startDate") LocalDateTime startDate, 
                                            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId ORDER BY o.orderDateTime DESC")
    List<Order> findCustomerOrderHistory(@Param("customerId") Long customerId);
    
    @Query("SELECT AVG(o.total) FROM Order o WHERE o.status = 'DELIVERED'")
    Optional<BigDecimal> findAverageOrderTotal();
    
    @Query("SELECT o FROM Order o WHERE DATE(o.orderDateTime) = CURRENT_DATE ORDER BY o.orderDateTime DESC")
    List<Order> findTodaysOrders();
}