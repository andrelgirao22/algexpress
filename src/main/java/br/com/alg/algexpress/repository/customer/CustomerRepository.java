package br.com.alg.algexpress.repository.customer;

import br.com.alg.algexpress.domain.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}