package br.com.alg.algexpress.infra.repository.customer;

import br.com.alg.algexpress.domain.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    Optional<Customer> findByPhone(String phone);
    
    Optional<Customer> findByEmail(String email);
    
    List<Customer> findByStatus(Customer.CustomerStatus status);
    
    List<Customer> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT c FROM Customer c WHERE c.phone = :phone OR c.email = :email")
    Optional<Customer> findByPhoneOrEmail(@Param("phone") String phone, @Param("email") String email);
    
    @Query("SELECT c FROM Customer c WHERE c.loyaltyPoints >= :minPoints ORDER BY c.loyaltyPoints DESC")
    List<Customer> findCustomersWithMinimumLoyaltyPoints(@Param("minPoints") Integer minPoints);
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.status = :status")
    Long countByStatus(@Param("status") Customer.CustomerStatus status);
    
    boolean existsByPhone(String phone);
    
    boolean existsByEmail(String email);

    List<Customer> findByNameContaining(String name);
    
    // Additional methods used by CustomerService
    
    List<Customer> findByLoyaltyPointsGreaterThan(Integer points);
    
    @Query("SELECT c FROM Customer c WHERE c.registrationDate BETWEEN :startDate AND :endDate")
    List<Customer> findCustomersCreatedBetween(@Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT c FROM Customer c LEFT JOIN Order o ON c.id = o.customer.id " +
           "GROUP BY c.id ORDER BY COUNT(o) DESC")
    List<Customer> findCustomersWithMostOrders();
    
    @Query("SELECT c FROM Customer c LEFT JOIN Order o ON c.id = o.customer.id " +
           "GROUP BY c.id ORDER BY COALESCE(SUM(o.total), 0) DESC")
    List<Customer> findCustomersWithHighestSpending();
    
    @Query("SELECT AVG(c.loyaltyPoints) FROM Customer c WHERE c.loyaltyPoints > 0")
    Optional<BigDecimal> findAverageLoyaltyPoints();
    
    @Query("SELECT " +
           "CASE " +
           "  WHEN c.loyaltyPoints = 0 THEN '0 points' " +
           "  WHEN c.loyaltyPoints BETWEEN 1 AND 50 THEN '1-50 points' " +
           "  WHEN c.loyaltyPoints BETWEEN 51 AND 100 THEN '51-100 points' " +
           "  WHEN c.loyaltyPoints BETWEEN 101 AND 200 THEN '101-200 points' " +
           "  ELSE '200+ points' " +
           "END as pointRange, " +
           "COUNT(c) as customerCount " +
           "FROM Customer c " +
           "GROUP BY " +
           "CASE " +
           "  WHEN c.loyaltyPoints = 0 THEN '0 points' " +
           "  WHEN c.loyaltyPoints BETWEEN 1 AND 50 THEN '1-50 points' " +
           "  WHEN c.loyaltyPoints BETWEEN 51 AND 100 THEN '51-100 points' " +
           "  WHEN c.loyaltyPoints BETWEEN 101 AND 200 THEN '101-200 points' " +
           "  ELSE '200+ points' " +
           "END " +
           "ORDER BY pointRange")
    List<Object[]> findLoyaltyPointsDistribution();
}