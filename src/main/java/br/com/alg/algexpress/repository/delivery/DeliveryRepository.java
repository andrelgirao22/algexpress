package br.com.alg.algexpress.repository.delivery;

import br.com.alg.algexpress.domain.delivery.Delivery;
import br.com.alg.algexpress.domain.delivery.DeliveryPerson;
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
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    
    Optional<Delivery> findByOrder(Order order);
    
    Optional<Delivery> findByOrderId(Long orderId);
    
    List<Delivery> findByDeliveryPerson(DeliveryPerson deliveryPerson);
    
    List<Delivery> findByDeliveryPersonId(Long deliveryPersonId);
    
    List<Delivery> findByStatus(Delivery.DeliveryStatus status);
    
    @Query("SELECT d FROM Delivery d WHERE d.deliveryPerson.id = :deliveryPersonId AND d.status = :status")
    List<Delivery> findByDeliveryPersonIdAndStatus(@Param("deliveryPersonId") Long deliveryPersonId, 
                                                   @Param("status") Delivery.DeliveryStatus status);
    
    @Query("SELECT d FROM Delivery d WHERE d.departureTime BETWEEN :startDate AND :endDate")
    List<Delivery> findDeliveriesBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT d FROM Delivery d WHERE d.status IN ('WAITING_DELIVERY_PERSON', 'EN_ROUTE', 'DELIVERY_ATTEMPT') ORDER BY d.order.orderDateTime ASC")
    List<Delivery> findActiveDeliveries();
    
    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.status = :status")
    Long countByStatus(@Param("status") Delivery.DeliveryStatus status);
    
    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.deliveryPerson.id = :deliveryPersonId AND d.status = 'DELIVERED'")
    Long countDeliveredByDeliveryPerson(@Param("deliveryPersonId") Long deliveryPersonId);
    
    @Query(value = "SELECT AVG(EXTRACT(EPOCH FROM (delivery_time - departure_time))/60) FROM deliveries " +
                   "WHERE departure_time IS NOT NULL AND delivery_time IS NOT NULL AND status = 'DELIVERED'", 
           nativeQuery = true)
    Optional<Double> findAverageDeliveryTimeMinutes();
    
    @Query("SELECT d.deliveryPerson, COUNT(d) as deliveryCount FROM Delivery d " +
           "WHERE d.status = 'DELIVERED' AND d.deliveryTime BETWEEN :startDate AND :endDate " +
           "GROUP BY d.deliveryPerson ORDER BY deliveryCount DESC")
    List<Object[]> findDeliveryPersonPerformance(@Param("startDate") LocalDateTime startDate, 
                                                 @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(d.deliveryFee) FROM Delivery d WHERE d.status = 'DELIVERED' AND " +
           "d.deliveryTime BETWEEN :startDate AND :endDate")
    Optional<BigDecimal> sumDeliveryFeesByDateRange(@Param("startDate") LocalDateTime startDate, 
                                                   @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT AVG(d.distanceKm) FROM Delivery d WHERE d.distanceKm IS NOT NULL AND d.status = 'DELIVERED'")
    Optional<BigDecimal> findAverageDeliveryDistance();
    
    @Query("SELECT d FROM Delivery d WHERE d.deliveryAttempts > :attempts")
    List<Delivery> findDeliveriesWithMultipleAttempts(@Param("attempts") Integer attempts);
    
    @Query("SELECT d FROM Delivery d WHERE d.status = 'CANCELLED' AND d.cancellationReason IS NOT NULL")
    List<Delivery> findCancelledDeliveriesWithReason();
    
    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.deliveryPerson.id = :deliveryPersonId " +
           "AND d.departureTime >= :startOfDay AND d.departureTime < :endOfDay")
    Long countTodaysDeliveriesByPerson(@Param("deliveryPersonId") Long deliveryPersonId, 
                                       @Param("startOfDay") LocalDateTime startOfDay, 
                                       @Param("endOfDay") LocalDateTime endOfDay);
    
    @Query("SELECT d FROM Delivery d WHERE d.departureTime >= :startOfDay AND d.departureTime < :endOfDay ORDER BY d.departureTime DESC")
    List<Delivery> findTodaysDeliveries(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}