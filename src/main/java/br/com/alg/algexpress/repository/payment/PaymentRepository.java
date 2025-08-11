package br.com.alg.algexpress.repository.payment;

import br.com.alg.algexpress.domain.order.Order;
import br.com.alg.algexpress.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    List<Payment> findByOrder(Order order);
    
    List<Payment> findByOrderId(Long orderId);
    
    List<Payment> findByStatus(Payment.PaymentStatus status);
    
    List<Payment> findByPaymentMethod(Payment.PaymentMethod paymentMethod);
    
    @Query("SELECT p FROM Payment p WHERE p.order.id = :orderId AND p.status = :status")
    List<Payment> findByOrderIdAndStatus(@Param("orderId") Long orderId, 
                                        @Param("status") Payment.PaymentStatus status);
    
    @Query("SELECT p FROM Payment p WHERE p.paymentDateTime BETWEEN :startDate AND :endDate")
    List<Payment> findPaymentsBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'APPROVED' AND p.order.id = :orderId")
    Optional<BigDecimal> sumApprovedPaymentsByOrderId(@Param("orderId") Long orderId);
    
    @Query("SELECT p FROM Payment p WHERE p.status = 'APPROVED' ORDER BY p.paymentDateTime DESC")
    List<Payment> findApprovedPaymentsOrderByDate();
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = :status")
    Long countByStatus(@Param("status") Payment.PaymentStatus status);
    
    @Query("SELECT p.paymentMethod, COUNT(p) FROM Payment p WHERE p.status = 'APPROVED' " +
           "GROUP BY p.paymentMethod ORDER BY COUNT(p) DESC")
    List<Object[]> findPaymentMethodStatistics();
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'APPROVED' AND " +
           "p.paymentDateTime BETWEEN :startDate AND :endDate")
    Optional<BigDecimal> sumApprovedPaymentsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                                       @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT p FROM Payment p WHERE p.transactionId = :transactionId")
    Optional<Payment> findByTransactionId(@Param("transactionId") String transactionId);
    
    @Query("SELECT p FROM Payment p WHERE p.authorizationCode = :authorizationCode")
    Optional<Payment> findByAuthorizationCode(@Param("authorizationCode") String authorizationCode);
    
    @Query("SELECT p FROM Payment p WHERE p.paymentMethod = :paymentMethod AND p.status = 'APPROVED' " +
           "AND p.paymentDateTime BETWEEN :startDate AND :endDate")
    List<Payment> findApprovedPaymentsByMethodAndDateRange(@Param("paymentMethod") Payment.PaymentMethod paymentMethod,
                                                          @Param("startDate") LocalDateTime startDate,
                                                          @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT AVG(p.amount) FROM Payment p WHERE p.status = 'APPROVED'")
    Optional<BigDecimal> findAverageApprovedPaymentAmount();
    
    @Query("SELECT p FROM Payment p WHERE p.paymentMethod = 'CASH' AND p.change > 0")
    List<Payment> findCashPaymentsWithChange();
    
    @Query("SELECT p FROM Payment p WHERE DATE(p.paymentDateTime) = CURRENT_DATE AND p.status = 'APPROVED'")
    List<Payment> findTodaysApprovedPayments();
}