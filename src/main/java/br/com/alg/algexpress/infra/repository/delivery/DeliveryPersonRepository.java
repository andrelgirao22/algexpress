package br.com.alg.algexpress.infra.repository.delivery;

import br.com.alg.algexpress.domain.delivery.DeliveryPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson, Long> {
    
    List<DeliveryPerson> findByStatus(DeliveryPerson.DeliveryPersonStatus status);
    
    List<DeliveryPerson> findByAvailableTrue();
    
    List<DeliveryPerson> findByVehicleType(DeliveryPerson.VehicleType vehicleType);
    
    Optional<DeliveryPerson> findByDocument(String document);
    
    Optional<DeliveryPerson> findByEmail(String email);
    
    Optional<DeliveryPerson> findByPhone(String phone);
    
    @Query("SELECT dp FROM DeliveryPerson dp WHERE dp.status = 'ACTIVE' AND dp.available = true")
    List<DeliveryPerson> findAvailableDeliveryPersons();
    
    @Query("SELECT dp FROM DeliveryPerson dp WHERE dp.status = 'ACTIVE' AND dp.available = true " +
           "AND dp.vehicleType = :vehicleType")
    List<DeliveryPerson> findAvailableByVehicleType(@Param("vehicleType") DeliveryPerson.VehicleType vehicleType);
    
    @Query("SELECT COUNT(dp) FROM DeliveryPerson dp WHERE dp.status = :status")
    Long countByStatus(@Param("status") DeliveryPerson.DeliveryPersonStatus status);
    
    @Query("SELECT COUNT(dp) FROM DeliveryPerson dp WHERE dp.status = 'ACTIVE' AND dp.available = true")
    Long countAvailable();
    
    @Query("SELECT dp FROM DeliveryPerson dp WHERE dp.shiftStart <= :currentTime AND dp.shiftEnd >= :currentTime " +
           "AND dp.status = 'ACTIVE'")
    List<DeliveryPerson> findDeliveryPersonsInShift(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT dp.vehicleType, COUNT(dp) FROM DeliveryPerson dp WHERE dp.status = 'ACTIVE' " +
           "GROUP BY dp.vehicleType")
    List<Object[]> findVehicleTypeStatistics();
    
    @Query("SELECT dp FROM DeliveryPerson dp WHERE dp.name LIKE %:name% AND dp.status = 'ACTIVE'")
    List<DeliveryPerson> findByNameContainingIgnoreCaseAndActive(@Param("name") String name);
    
    boolean existsByDocument(String document);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhone(String phone);
    
    @Query("SELECT dp FROM DeliveryPerson dp WHERE dp.vehiclePlate = :plate")
    Optional<DeliveryPerson> findByVehiclePlate(@Param("plate") String plate);
    
    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.deliveryPerson.id = :deliveryPersonId " +
           "AND d.status = 'DELIVERED' AND d.deliveryTime BETWEEN :startDate AND :endDate")
    Long countDeliveriesByPersonAndDateRange(@Param("deliveryPersonId") Long deliveryPersonId,
                                            @Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);
}