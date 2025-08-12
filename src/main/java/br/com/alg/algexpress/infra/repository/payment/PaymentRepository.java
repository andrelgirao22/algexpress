package br.com.alg.algexpress.infra.repository.payment;

import br.com.alg.algexpress.domain.order.Order;
import br.com.alg.algexpress.domain.payment.Payment;
import br.com.alg.algexpress.domain.user.User;
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
    
    @Query("SELECT p FROM Payment p WHERE p.paymentMethod.type = :paymentMethodType")
    List<Payment> findByPaymentMethodType(@Param("paymentMethodType") br.com.alg.algexpress.domain.valueObjects.PaymentMethod.PaymentType paymentMethodType);
    
    @Query("SELECT p FROM Payment p WHERE p.order.id = :orderId AND p.status = :status")
    List<Payment> findByOrderIdAndStatus(@Param("orderId") Long orderId, 
                                        @Param("status") Payment.PaymentStatus status);
    
    @Query("SELECT p FROM Payment p WHERE p.paymentDateTime BETWEEN :startDate AND :endDate")
    List<Payment> findPaymentsBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p FROM Payment p WHERE p.paymentDateTime BETWEEN :startDate AND :endDate AND p.status = 'APPROVED'")
    List<Payment> findPaymentsAppovedBetweenDates(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(p.amount.amount) FROM Payment p WHERE p.status = 'APPROVED' AND p.order.id = :orderId")
    Optional<BigDecimal> sumApprovedPaymentsByOrderId(@Param("orderId") Long orderId);
    
    @Query("SELECT p FROM Payment p WHERE p.status = 'APPROVED' ORDER BY p.paymentDateTime DESC")
    List<Payment> findApprovedPaymentsOrderByDate();
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = :status")
    Long countByStatus(@Param("status") Payment.PaymentStatus status);
    
    @Query("SELECT p.paymentMethod.type, COUNT(p) FROM Payment p WHERE p.status = 'APPROVED' " +
           "GROUP BY p.paymentMethod.type ORDER BY COUNT(p) DESC")
    List<Object[]> findPaymentMethodStatistics();
    
    @Query("SELECT SUM(p.amount.amount) FROM Payment p WHERE p.status = 'APPROVED' AND " +
           "p.paymentDateTime BETWEEN :startDate AND :endDate")
    Optional<BigDecimal> sumApprovedPaymentsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                                       @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT p FROM Payment p WHERE p.transactionInfo.transactionId = :transactionId")
    Optional<Payment> findByTransactionId(@Param("transactionId") String transactionId);
    
    @Query("SELECT p FROM Payment p WHERE p.transactionInfo.authorizationCode = :authorizationCode")
    Optional<Payment> findByAuthorizationCode(@Param("authorizationCode") String authorizationCode);
    
    @Query("SELECT p FROM Payment p WHERE p.paymentMethod.type = :paymentMethodType AND p.status = 'APPROVED' " +
           "AND p.paymentDateTime BETWEEN :startDate AND :endDate")
    List<Payment> findApprovedPaymentsByMethodAndDateRange(@Param("paymentMethodType") br.com.alg.algexpress.domain.valueObjects.PaymentMethod.PaymentType paymentMethodType,
                                                          @Param("startDate") LocalDateTime startDate,
                                                          @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT AVG(p.amount.amount) FROM Payment p WHERE p.status = 'APPROVED'")
    Optional<BigDecimal> findAverageApprovedPaymentAmount();
    
    @Query("SELECT p FROM Payment p WHERE p.paymentMethod.type = 'CASH' AND p.change.amount > 0")
    List<Payment> findCashPaymentsWithChange();
    
    @Query("SELECT p FROM Payment p WHERE p.paymentDateTime >= :startOfDay AND p.status = 'APPROVED'")
    List<Payment> findTodaysApprovedPayments(@Param("startOfDay") LocalDateTime startOfDay);

}