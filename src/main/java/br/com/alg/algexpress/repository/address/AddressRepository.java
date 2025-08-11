package br.com.alg.algexpress.repository.address;

import br.com.alg.algexpress.domain.address.Address;
import br.com.alg.algexpress.domain.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
    List<Address> findByCustomer(Customer customer);
    
    List<Address> findByCustomerId(Long customerId);
    
    List<Address> findByType(Address.AddressType type);
    
    List<Address> findByCity(String city);
    
    List<Address> findByNeighborhood(String neighborhood);
    
    @Query("SELECT a FROM Address a WHERE a.customer.id = :customerId AND a.type = :type")
    List<Address> findByCustomerIdAndType(@Param("customerId") Long customerId, @Param("type") Address.AddressType type);
    
    @Query("SELECT a FROM Address a WHERE a.distanceKm <= :maxDistance ORDER BY a.distanceKm ASC")
    List<Address> findAddressesWithinDistance(@Param("maxDistance") BigDecimal maxDistance);
    
    @Query("SELECT a FROM Address a WHERE a.deliveryFee BETWEEN :minFee AND :maxFee")
    List<Address> findByDeliveryFeeBetween(@Param("minFee") BigDecimal minFee, @Param("maxFee") BigDecimal maxFee);
    
    @Query("SELECT a FROM Address a WHERE a.zipCode = :zipCode")
    List<Address> findByZipCode(@Param("zipCode") String zipCode);
    
    @Query("SELECT DISTINCT a.city FROM Address a ORDER BY a.city")
    List<String> findAllCities();
    
    @Query("SELECT DISTINCT a.neighborhood FROM Address a WHERE a.city = :city ORDER BY a.neighborhood")
    List<String> findNeighborhoodsByCity(@Param("city") String city);
    
    @Query("SELECT AVG(a.deliveryFee) FROM Address a WHERE a.deliveryFee IS NOT NULL")
    Optional<BigDecimal> findAverageDeliveryFee();
}